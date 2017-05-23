package common

import java.io.{BufferedReader, File, FileInputStream, InputStreamReader}

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConversions._
import scala.meta._



object Loaders extends Utility {

  def getKeyListFromHocon(filePath: String, hoconPath: Option[String]): Seq[String] = {
    val conf: Config = ConfigFactory.parseFile(new File(filePath))
    getKeyListFromHocon(conf, hoconPath)
  }

  def getKeyListFromHocon(conf: Config, hoconPath: Option[String]): Seq[String] = {
    hoconPath match {
      case Some(c) => getKeyListFromHocon(conf.getConfig(c))
      case None    => getKeyListFromHocon(conf)
    }
  }

  def getKeyListFromHocon(conf: Config, hoconPath: String): Seq[String] = {
    getKeyListFromHocon(conf.getConfig(hoconPath))
  }

  def getKeyListFromHocon(conf: Config): Seq[String] = {
    var lst = List[String]()
    for {
      tbl <- conf.entrySet()
    } yield {
      lst = lst :+ tbl.getKey
    }
    lst.map(x => x.toString).sortBy(f => f)
  }

  def getConfTableColumn(conf: Config): Option[Seq[ConfTableColumn]] = {
    tryOpt(
      for {
        tbl <- conf.getConfigList("columnList")
      } yield {
        ConfTableColumn(
          tbl.getString("cName"),
          tbl.getString("cType"),
          tbl.getStringList("cAttr")
        )
      }
    )
  }

  def getConfTable(conf: Config): Option[Seq[ConfTable]] = tryOpt(
    for {
      tbl <- conf.getConfigList("scaffold.table.tableList")
    } yield {
      val keyListInTable = getKeyListFromHocon(tbl, None)
      ConfTable(
        tbl.getString("tableName"),
        tbl.getString("relation"),
        tbl.getStringList("isScaffoldList"),
        tbl.getStringList("isRollList"),
        getConfTableColumn(tbl).get
      )
    }
  )

  def getConfDto(conf: Config) = { ConfDto(
    dbDb = conf.getString("scaffold.db.db"),
    dbDefaultDriver = conf.getString("scaffold.db.default.driver"),
    dbDefaultPassword = conf.getString("scaffold.db.default.password"),
    dbDefaultUrl = conf.getString("scaffold.db.default.url"),
    dbDefaultUsername = conf.getString("scaffold.db.default.username"),
    dbIsUseDocker = conf.getString("scaffold.db.isUseDocker"),
    generalOutputDirectoryType = conf.getString("scaffold.general.output.directory.type"),
    generalOutputOptions = conf.getStringList("scaffold.general.output.options"),
    generalOutputPathController = conf.getString("scaffold.general.output.path.controller"),
    generalOutputPathDao = conf.getString("scaffold.general.output.path.dao"),
    generalOutputPathForm = conf.getString("scaffold.general.output.path.form"),
    generalOutputPathService = conf.getString("scaffold.general.output.path.service"),
    generalOutputPathView = conf.getString("scaffold.general.output.path.view"),
    generalSlickPathTables = conf.getString("scaffold.general.slick.path.tables"),
    modulesAuditLogIsUse = conf.getString("scaffold.modules.auditLog.isUse"),
    modulesAuditLogTableTableName = conf.getString("scaffold.modules.auditLog.table.tableName"),
    modulesAuthIsUse = conf.getString("scaffold.modules.auth.isUse"),
    modulesAuthLibrary = conf.getString("scaffold.modules.auth.library"),
    modulesAuthRoleList = conf.getStringList("scaffold.modules.auth.roleList"),
    modulesAuthTableLoginIdColumnName = conf.getString("scaffold.modules.auth.table.LoginIdColumnName"),
    modulesAuthTableLoginPassColumnName = conf.getString("scaffold.modules.auth.table.LoginPassColumnName"),
    modulesAuthTableAccountTableName = conf.getString("scaffold.modules.auth.table.accountTableName"),
    modulesDbmigrationIsUse = conf.getString("scaffold.modules.dbmigration.isUse"),
    modulesDbmigrationLibrary = conf.getString("scaffold.modules.dbmigration.library"),
    modulesDbmigrationPathMigration = conf.getString("scaffold.modules.dbmigration.path.migration"),
    modulesErrorLogIsUse = conf.getString("scaffold.modules.errorLog.isUse"),
    modulesErrorLogTableTableName = conf.getString("scaffold.modules.errorLog.table.tableName"),
    modulesI18nCrudTableI18nList = conf.getStringList("scaffold.modules.i18nCrudTable.i18nList"),
    modulesI18nCrudTableIsUse = conf.getString("scaffold.modules.i18nCrudTable.isUse"),
    modulesI18nCrudTableTableTableName = conf.getString("scaffold.modules.i18nCrudTable.table.tableName"),
    modulesI18nMessageConfI18nList = conf.getStringList("scaffold.modules.i18nMessageConf.i18nList"),
    modulesI18nMessageConfIsUse = conf.getString("scaffold.modules.i18nMessageConf.isUse"),
    modulesSlickcodegenIsUse = conf.getString("scaffold.modules.slickcodegen.isUse"),
    modulesSlickcodegenLibrary = conf.getString("scaffold.modules.slickcodegen.library"),
    modulesTwirlScaffoldThemesEnableList = conf.getStringList("scaffold.modules.twirlScaffoldThemes.enableList"),
    modulesTwirlScaffoldThemesIsUse = conf.getString("scaffold.modules.twirlScaffoldThemes.isUse"),
    modulesTwirlScaffoldThemesSourceAccount = conf.getString("scaffold.modules.twirlScaffoldThemes.source.account"),
    modulesTwirlScaffoldThemesSourceBackendAdmin = conf.getString("scaffold.modules.twirlScaffoldThemes.source.backend.admin"),
    modulesTwirlScaffoldThemesSourceFrontBlog = conf.getString("scaffold.modules.twirlScaffoldThemes.source.front.blog"),
    modulesTwirlScaffoldThemesSourceFrontCorporate = conf.getString("scaffold.modules.twirlScaffoldThemes.source.front.corporate"),
    modulesTwirlScaffoldThemesSourceFrontStatusErrors = conf.getString("scaffold.modules.twirlScaffoldThemes.source.front.statusErrors"),
    tableTableList = getConfTable(conf).get) }
  case class ConfDto(
                      dbDb : String,
                      dbDefaultDriver : String,
                      dbDefaultPassword : String,
                      dbDefaultUrl : String,
                      dbDefaultUsername : String,
                      dbIsUseDocker : String,
                      generalOutputDirectoryType : String,
                      generalOutputOptions : Seq[String],
                      generalOutputPathController : String,
                      generalOutputPathDao : String,
                      generalOutputPathForm : String,
                      generalOutputPathService : String,
                      generalOutputPathView : String,
                      generalSlickPathTables : String,
                      modulesAuditLogIsUse : String,
                      modulesAuditLogTableTableName : String,
                      modulesAuthIsUse : String,
                      modulesAuthLibrary : String,
                      modulesAuthRoleList : Seq[String],
                      modulesAuthTableLoginIdColumnName : String,
                      modulesAuthTableLoginPassColumnName : String,
                      modulesAuthTableAccountTableName : String,
                      modulesDbmigrationIsUse : String,
                      modulesDbmigrationLibrary : String,
                      modulesDbmigrationPathMigration : String,
                      modulesErrorLogIsUse : String,
                      modulesErrorLogTableTableName : String,
                      modulesI18nCrudTableI18nList : Seq[String],
                      modulesI18nCrudTableIsUse : String,
                      modulesI18nCrudTableTableTableName : String,
                      modulesI18nMessageConfI18nList : Seq[String],
                      modulesI18nMessageConfIsUse : String,
                      modulesSlickcodegenIsUse : String,
                      modulesSlickcodegenLibrary : String,
                      modulesTwirlScaffoldThemesEnableList : Seq[String],
                      modulesTwirlScaffoldThemesIsUse : String,
                      modulesTwirlScaffoldThemesSourceAccount : String,
                      modulesTwirlScaffoldThemesSourceBackendAdmin : String,
                      modulesTwirlScaffoldThemesSourceFrontBlog : String,
                      modulesTwirlScaffoldThemesSourceFrontCorporate : String,
                      modulesTwirlScaffoldThemesSourceFrontStatusErrors : String,
                      tableTableList : Seq[ConfTable])


  def getSlickTableListFromCaseClass(caseClass: String): Option[GeneratedTable] = {
    val clsx: Source = caseClass.parse[Source].get
    clsx.collect {
      case cls: Defn.Class =>
        GeneratedTable(
          cls.name.syntax.toString,
          cls.ctor.paramss.flatten.map { param =>
            val className = cls.name.syntax
            val fieldName = Term.Name(param.name.syntax)
            val fieldType = param.decltpe.getOrElse(abort(s"${param.name.syntax} does not have type"))
            val argName = Term.fresh("text")
            val returnType = Type.Name(fieldType.syntax)
            //            System.out.println(s"""${className}\t${fieldName}\t$fieldType""");
            //            System.out.println(s"""def $fieldName($argName: $fieldType):(_root_.scala.Predef.String, $returnType) = (${fieldName.text}, $argName)""");
            GeneratedColumn(fieldName.toString(), fieldType.toString())
          }
        )
    }.headOption
  }

  def getSlickTableList(setting: ConfDto): Seq[GeneratedTable] = {
    val modelpath = setting.generalSlickPathTables
    val inBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(modelpath), "UTF-8"))
    val lines = Iterator.continually(inBuffer.readLine()).takeWhile(_ != null).toList
    (for ((line, lineNum) <- lines.zipWithIndex) yield line.replaceAll("^\\s*", ""))
      .filter(_.startsWith("case class"))
      .map(x => getSlickTableListFromCaseClass(x))
      .filter(_.isDefined)
      .map(_.get)
  }

}