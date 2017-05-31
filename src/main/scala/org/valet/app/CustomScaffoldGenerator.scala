package org.valet.app

import java.io.File

import org.joda.time.DateTime
import org.valet.common.ConfTableColumn
import skinny.task.generator.ScaffoldGenerator

object CustomScaffoldGenerator extends ScaffoldGenerator {


  def toAttr(t: String): String = {
    extractTypeIfOptionOrSeq(t) match {
      case "PRIMARY_KEY"    => " primary key"
      case "AUTO_INCREMENT" => " auto_increment"
      case "UNIQUE"         => " unique"
      case _                => ""
    }
  }

  //  override def resourceDir = "src/main"

  def customGenerateMigrationSQL(resources: String, resource: String, newArgs: Seq[ConfTableColumn], dirPath: String, num: Int) {
    val version: Long = DateTime.now.getMillis + (num * 100)
    val file: File = {
      val filepath = {
        val filename: String = s"V${version}__Create_${resources}_table.sql"
        s"${dirPath}/${filename}"
      }
      new File(filepath)
    }
    val sql = customMigrationSQL(resources, resource, newArgs)
    writeIfAbsent(file, sql)
  }

  def customMigrationSQL(resources: String, resource: String, srcCols: Seq[ConfTableColumn]): String = {
    val name = tableName.getOrElse(toSnakeCase(resources))
    val columns = srcCols.map { a =>

      val defaultValue = a.cAttr.filter(_.startsWith("DEFAULT")).map(x => " " + x.replace(":", " ")).headOption.getOrElse("")
      val defaultOptional = (if (isOptionClassName(a.cType)) "" else " not null")

      val fieldName = toSnakeCase(a.cName)
      val fieldType = toDBType(a.cType)
      val default = defaultValue + defaultOptional
      val attr = a.cAttr.map(x => toAttr(x)).filter(f => !f.isEmpty).mkString("")

      s"  ${fieldName} ${fieldType}${default}${attr}"

    }.mkString(",\n")

    s"""-- For H2 Database : Please add suitable restrictions if needed.
       |create table ${name} (
       |${columns}
       |)
       |""".stripMargin
  }

  override def formHtmlCode(namespaces: Seq[String], resources: String, resource: String, nameAndTypeNamePairs: Seq[(String, String)]): String = ""

  override def newHtmlCode(namespaces: Seq[String], resources: String, resource: String, nameAndTypeNamePairs: Seq[(String, String)]): String = ""

  override def editHtmlCode(namespaces: Seq[String], resources: String, resource: String, nameAndTypeNamePairs: Seq[(String, String)]): String = ""

  override def indexHtmlCode(namespaces: Seq[String], resources: String, resource: String, nameAndTypeNamePairs: Seq[(String, String)]): String = ""

  override def showHtmlCode(namespaces: Seq[String], resources: String, resource: String, nameAndTypeNamePairs: Seq[(String, String)]): String = ""


}