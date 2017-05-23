package org.valet.app

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import common.{Loaders, ValetUtility}
import skinny.task.generator.{ScaffoldGeneratorArg, ScaffoldSspGenerator}

object Launcher extends ValetUtility {

  def main(args: Array[String]) {
    args.toList match {
      case name :: params if name == "cli"     => runCl(params, withId = true)
      case name :: params if name == "hocon"   => runHocon(params.headOption.getOrElse(new File(".").getCanonicalPath + "/webtools.conf"))
      case name :: params if name == "cli-all" => runClAll(params)
      case _                                   => showUsage()
    }
  }

  def runClAll(args: List[String]): Unit = {
    ScaffoldSspGenerator.run(args)
  }

  def runCl(args: List[String], withId: Boolean): Unit = {
    if (args.size < 3) {
      showUsage()
      return
    } else if (args.head.contains(":") || args(1).contains(":")) {
      showUsage()
      return
    }
    val completedArgs: Seq[String] = if (args(2).contains(":")) Seq("") ++ args else args

    completedArgs match {
      case ns :: rs :: r :: attributes =>
        val (namespaces, resources, resource) = (ns.split('.'), toFirstCharLower(rs), toFirstCharLower(r))
        val errorList = getErrorList(attributes)
        errorList.isEmpty match {
          case true  => CustomScaffoldGenerator.generateMigrationSQL(resources, resource, getGeneratorArgs(attributes), withId)
          case false => echoErrorMsg(errorList.mkString("\n"))
        }
      case _                           => echoErrorMsg("invalid args")
    }
  }

  def runHocon(filepath: String): Unit = {
    println(s"config file = $filepath");
    val conf: Config = ConfigFactory.parseFile(new File(filepath))
    val confDto = Loaders.getConfDto(conf)
    val list = confDto.tableTableList
    for ((tbl, num) <- list.zipWithIndex) {
      val errorList = getErrorList(tbl.columnList.map(col => col.cName + ":" + col.cType).toList)
      if (errorList.nonEmpty) {
        echoErrorMsg(errorList.mkString("\n"))
      } else {
        CustomScaffoldGenerator.customGenerateMigrationSQL(tbl.tableName, tbl.tableName, tbl.columnList, confDto.modulesDbmigrationPathMigration, num)
      }
    }
  }

  private def showUsage() = {
    println(
      s"""
         | Usage:
         |        sbt "run cli members member name:String activated:Boolean luckyNumber:Option[Long] birthday:Option[LocalDate]"
         |        sbt "run hocon $$(pwd)/webtools.conf"
       """.stripMargin
    )
  }

  private def getErrorList(attributes: List[String]): List[String] = {
    attributes.flatMap {
      case attr if !attr.contains(":") => Some(s"Invalid parameter ($attr) found. Scaffold parameter must be delimited with colon(:)")
      case attr                        => attr.split(":") match {
        case Array(_, paramType) if !CustomScaffoldGenerator.isSupportedParamType(paramType) && !CustomScaffoldGenerator.isAssociationTypeName(paramType) =>
          Some(s"Invalid type ($paramType) found. ")
        case _                                                                                                                                            => None
      }
    }.map(_.toString)
  }


  private def getGeneratorArgs(args: List[String]): Seq[ScaffoldGeneratorArg] = {
    args.flatMap { attribute =>
      attribute.toString.split(":") match {
        case Array(name, typeName, columnName) => Some(ScaffoldGeneratorArg(name, typeName, Some(columnName)))
        case Array(name, typeName)             => Some(ScaffoldGeneratorArg(name, typeName))
        case _                                 => None
      }
    }
  }


}
