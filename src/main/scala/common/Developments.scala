package common

import com.typesafe.config.Config
import org.valet.common.Loaders.getKeyListFromHocon

object Developments extends Utility {

  def getConfKeyValues(conf: Config): Unit = {

    val keyList = getKeyListFromHocon(conf)
//    val configJSON: String = conf.root().render(ConfigRenderOptions.concise())

    val f1 = keyList.map { key =>
      val fieldName = toFirstCharLower(key.split('.').tail.map(str => toFirstCharUpper(toCamelCase(str))).mkString(""))
      conf.getAnyRef(key).getClass.toString match {
        case "class java.lang.String"    => s"""  $fieldName = conf.getString("$key")"""
        case "class java.util.ArrayList" =>
          fieldName match {
            case "tableTableList" => s"""  $fieldName = getConfTable(conf).get"""
            case _                => s"""  $fieldName = conf.getStringList("$key")"""
          }
        case _                           => "----- error -----"
      }
    }.mkString(",\n")
    System.out.println("def getConfDto(conf: Config) = { ConfDto(\n" + f1 + ") }")

    /**
      * ScaffoldingSetting　Case Class用
      */
    val f2: String = keyList.map { key =>
      val fieldName = toFirstCharLower(key.split('.').tail.map(str => toFirstCharUpper(toCamelCase(str))).mkString(""))
      conf.getAnyRef(key).getClass.toString match {
        case "class java.lang.String"    => s"""  $fieldName : String"""
        case "class java.util.ArrayList" =>
          fieldName match {
            case "tableTableList" => s"""  $fieldName : Seq[ConfTable]"""
            case _                => s"""  $fieldName : Seq[String]"""
          }
        case _                           => "----- error -----"
      }
    }.mkString(",\n")
    System.out.println("case class ConfDto(\n" + f2 + ")")
  }

  def echoClassNames = {
    print(
      Seq(
        "Dao",
        "Service",
        "Form",
        "Controller"
      ).map { f =>
        s"""
           |  // ${f}
           |  def get${f}(generatedTable: GeneratedTable): String = {
           |    getTableName(generatedTable) + suffix${f}
           |  }
           |  def getAg${f}(generatedTable: GeneratedTable): String = {
           |    getAgTableName(generatedTable) + suffix${f}
           |  }
           |  def get${f}Like(generatedTable: GeneratedTable): String = {
           |    getLike(getTableName(generatedTable) + suffix${f})
           |  }
           |  def getAg${f}Like(generatedTable: GeneratedTable): String = {
           |    getLike(getAgTableName(generatedTable) + suffix${f})
           |  }
      """.stripMargin
      }.mkString("")
    )
  }
}
