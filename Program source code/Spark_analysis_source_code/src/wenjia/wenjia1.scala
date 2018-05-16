/**
  * Created by hadoop on 17-5-24.
  *
  */
import org.apache.spark.SparkConf
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client._
import org.apache.spark.SparkContext
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.api.java.JavaRDD
import scala.collection.mutable.ArrayBuffer
import HBase_Con._
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import java.util.{Date, Locale}
import java.text.SimpleDateFormat
import java.lang.NumberFormatException
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StructType,StructField,StringType}
import org.apache.spark.sql.SparkSession
import java.io.FileWriter

object wenjia1 {
  def main(args: Array[String]) {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val sparkConf = new SparkConf().setAppName("sji").setMaster("spark://clond1:7077")
    sparkConf.set("spark.testing.memory", "2147480000")
    val sc = new SparkContext(sparkConf)
    val sqcX=new SQLContext(sc)
     //准备训练集合
    /* val out_fli1 = new FileWriter("/home/hadoop/Documents/dataa",true)
     out_fli1.write("6,9,200\n")
    out_fli1.write("6,9,200\n")
     out_fli1.close()
     val raw_data=sc.textFile("/home/hadoop/Documents/dataa")
     val map_data=raw_data.map{x=>
       val split_list=x.split(",")
       (split_list(0).toInt,split_list(1).toInt,split_list(2).toInt)
     }
     val dfx=sqcX.createDataFrame(map_data)
     val datax = dfx.toDF("month", "day" ,"keliu")
     val colArray = Array("month", "day")
     val assembler = new VectorAssembler().setInputCols(colArray).setOutputCol("features")
     val vecDF: DataFrame = assembler.transform(datax)


     val raw_data_predict=sc.textFile("/home/hadoop/Documents/xx")
     val map_data_for_predict=raw_data_predict.map{x=>
       val split_list=x.split(",")
       (split_list(0).toInt,split_list(1).toInt,split_list(2).toInt)
     }
     val df_for_predict=sqcX.createDataFrame(map_data_for_predict)
     val data_for_predict = df_for_predict.toDF("month", "day" ,"keliu")
     val colArray_for_predict = Array("month", "day")
     val assembler_for_predict = new VectorAssembler().setInputCols(colArray_for_predict).setOutputCol("features")
     val vecDF_for_predict: DataFrame = assembler_for_predict.transform(data_for_predict)
     // 建立模型，预测keliu
     // 设置线性回归参数
     val lr1 = new LinearRegression()
     val lr2 = lr1.setFeaturesCol("features").setLabelCol("keliu").setFitIntercept(true)
     // RegParam：正则化
     val lr3 = lr2.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
     val lr = lr3

     // 将训练集合代入模型进行训练
     val lrModel = lr.fit(vecDF)
     // 输出模型全部参数
     lrModel.extractParamMap()
     val predictions: DataFrame = lrModel.transform(vecDF_for_predict)
     println("输出预测结果")
     val predict_result: DataFrame =predictions.selectExpr("prediction")
     var listRow= predict_result.rdd.collect()
     var ms=listRow(0).toString()
     var redict_re=ms.slice(1,ms.length-1)
     println(redict_re.toDouble.toInt)
    var ms2=listRow(1).toString()
    var redict_re2=ms2.slice(1,ms2.length-1)
    println(redict_re2.toDouble.toInt)*/


    val config = HBaseConfiguration.create()
    val conn = ConnectionFactory.createConnection(config)
    def Hbase_count(name: String): Long = {
      config.set(TableInputFormat.INPUT_TABLE, name)
      val stuRDD = sc.newAPIHadoopRDD(config, classOf[TableInputFormat],
        classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
        classOf[org.apache.hadoop.hbase.client.Result])
      val count = stuRDD.count()
      count
    }

    /*Hbase_truncate(conn,"00aabbcc")
     Hbase_truncate(conn,"temp_log")
     Hbase_truncate(conn,"store_temp")
     Hbase_truncate(conn,"store_log")
     Hbase_truncate(conn,"store_time")
     Hbase_truncate(conn,"human_times")
     Hbase_truncate(conn,"human_history")
     Hbase_truncate(conn,"new_old_history")
     Hbase_truncate(conn,"count_human_times")
     Hbase_truncate(conn,"shop_times")
     Hbase_truncate(conn,"interview_times")
     Hbase_truncate(conn,"jump_log")
     Hbase_truncate(conn,"deep_log")
     Hbase_truncate(conn,"activity_log")*/
    var A: ArrayBuffer[People] = null
    var B: ArrayBuffer[People] = null
    //次数define
    var x_num: Array[Int] = new Array[Int](6)
    var t_num: Array[Int] = new Array[Int](7)
    var h_num: Array[Int] = new Array[Int](7)

    var new_people: Long = 0
    var old_people: Long = 0

    var last_hour = new String
    var last_day: Int = 24
    var last_week: Int = 24 * 7
    var last_month: Int = 24 * 30
    var ke_hour: Long = 0
    var ke_day: Long = 0
    var ke_week: Long = 0
    var ke_month: Long = 0
    var ru_hour: Long = 0
    var ru_day: Long = 0
    var ru_week: Long = 0
    var ru_month: Long = 0
    var zq1_hour: Long = 0
    var zq1_day: Long = 0
    var zq1_week: Long = 0
    var zq1_month: Long = 0
    var zq2_hour: Long = 0
    var zq2_day: Long = 0
    var zq2_week: Long = 0
    var zq2_month: Long = 0
    var zq3_hour: Long = 0
    var zq3_day: Long = 0
    var zq3_week: Long = 0
    var zq3_month: Long = 0
    var zq4_hour: Long = 0
    var zq4_day: Long = 0
    var zq4_week: Long = 0
    var zq4_month: Long = 0
    var zq5_hour: Long = 0
    var zq5_day: Long = 0
    var zq5_week: Long = 0
    var zq5_month: Long = 0
    var rur_hour: Double = 0
    var rur_day: Double = 0
    var rur_week: Double = 0
    var rur_month: Double = 0
    var new_hour: Long = 0
    var new_day: Long = 0
    var new_week: Long = 0
    var new_month: Long = 0
    var old_hour: Long = 0
    var old_day: Long = 0
    var old_week: Long = 0
    var old_month: Long = 0
    var zd1_hour: Long = 0
    var zd1_day: Long = 0
    var zd1_week: Long = 0
    var zd1_month: Long = 0
    var zd2_hour: Long = 0
    var zd2_day: Long = 0
    var zd2_week: Long = 0
    var zd2_month: Long = 0
    var zd3_hour: Long = 0
    var zd3_day: Long = 0
    var zd3_week: Long = 0
    var zd3_month: Long = 0
    var zd4_hour: Long = 0
    var zd4_day: Long = 0
    var zd4_week: Long = 0
    var zd4_month: Long = 0
    var flag_xx = new String
    var flag_first = false
    val df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH)
    val df2 = new SimpleDateFormat("HH", Locale.ENGLISH)
    val df_day = new SimpleDateFormat("dd", Locale.ENGLISH)
    val df_week = new SimpleDateFormat("EEE", Locale.ENGLISH)
    val df_month = new SimpleDateFormat("MM", Locale.ENGLISH)
    def xxx10(tablename: String, key: String, values: String): Unit = {
      var hx = Hbase_get(conn, tablename, "1", key, "9")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "10", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "8")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "9", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "7")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "8", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "6")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "7", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "5")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "6", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "4")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "5", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "3")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "4", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "2")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "3", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "1")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "2", hx)
      }
      Hbase_insert(conn, tablename, "1", key, "1", values)
    }

    def xxx(tablename: String, key: String, values: String): Unit = {
      var hx = Hbase_get(conn, tablename, "1", key, "5")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "6", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "4")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "5", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "3")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "4", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "2")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "3", hx)
      }
      hx = Hbase_get(conn, tablename, "1", key, "1")
      if (hx != null) {
        Hbase_insert(conn, tablename, "1", key, "2", hx)
      }
      Hbase_insert(conn, tablename, "1", key, "1", values)
    }

    while(false) {

      val time1 = df.format(new Date())
      var count_all: Long = Hbase_count("00aabbcc")
      x_num(0) = 0
      x_num(1) = 0
      x_num(2) = 0
      x_num(3) = 0
      x_num(4) = 0
      x_num(5) = 0
      t_num(0) = 0
      t_num(1) = 0
      t_num(2) = 0
      t_num(3) = 0
      t_num(4) = 0
      h_num(0) = 0
      h_num(1) = 0
      h_num(2) = 0
      h_num(3) = 0
      //遍历temp
      A = Hbase_scan2(conn, "00aabbcc")
      val c = A.toArray
      var i = 0
      if (c.nonEmpty) {
        //插入客流量日志
        Hbase_insert(conn, "temp_log", A(0).time, "count", "1", count_all.toString)
        xxx10("keliu", "now", count_all.toString)
        //判断是进店铺还是出店铺
        if (c.length != 0) {
          var i=c.length
          while(i!=0){
            i-=1
            val human_in = Hbase_get(conn, "store_temp", A(i).mac, "time", "1")
            val human_last_time = Hbase_get(conn, "human_history", A(i).mac, "this_time", "1")
            if(A(i).range!=null) {
              if (A(i).range.toInt < 100) {

                //进入店铺的人，判断是否是刚进来的
                if (human_in == null) {
                  Hbase_insert(conn, "store_temp", A(i).mac, "time", "1", A(i).time)
                  //判断新老顾客，记录访问次数和时间
                  val values = Hbase_get(conn, "human_times", A(i).mac, "values", "1")
                  if (values == null) {
                    x_num(0) += 1
                    Hbase_insert(conn, "human_times", A(i).mac, "time", "1", A(i).time)
                    Hbase_insert(conn, "human_times", A(i).mac, "values", "1", "1")
                    Hbase_insert(conn, "human_history", A(i).mac, "this_time", "1", A(i).time)
                  }
                  else {
                    var x = values.toInt
                    x += 1
                    Hbase_insert(conn, "human_times", A(i).mac, "time", x.toString, A(i).time)
                    Hbase_insert(conn, "human_times", A(i).mac, "values", "1", x.toString)
                    //来访次数
                    if (x == 2) x_num(1) += 1
                    else if (x == 3) x_num(2) += 1
                    else if (x == 4) x_num(3) += 1
                    else if (x == 5) x_num(4) += 1
                    else x_num(5) += 1
                    Hbase_insert(conn, "human_history", A(i).mac, "last_time", "1", human_last_time)
                    Hbase_insert(conn, "human_history", A(i).mac, "this_time", "1", A(i).time)
                    val n = Times.Time_Duration_Minutes(A(i).time, human_last_time)
                    Hbase_insert(conn, "human_history", A(i).mac, "subtract", "1", n.toString)
                    //来访周期
                    if (n >= 0 && n < 1) t_num(0) += 1
                    else if (n < 2 && n >= 1) t_num(1) += 1
                    else if (n < 3 && n >= 2) t_num(2) += 1
                    else if (n < 4 && n >= 3) t_num(3) += 1
                    else t_num(4) += 1
                  }
                  Hbase_insert(conn, "store_time", A(i).mac, "intime", "1", A(i).time)
                  Hbase_insert(conn, "store_time", A(i).mac, "intime", "2", A(i).time)
                  Hbase_insert(conn, "store_time", A(i).mac, "subtract", "1", "1")

                }
                else {
                  var xskj = Hbase_get(conn, "human_times", A(i).mac, "values", "1")

                  if (xskj == null) {
                    xskj = "0"
                  }
                  val x = xskj.toInt
                  if (x == 1) x_num(0) += 1
                  else if (x == 2) x_num(1) += 1
                  else if (x == 3) x_num(2) += 1
                  else if (x == 4) x_num(3) += 1
                  else if (x == 5) x_num(4) += 1
                  else x_num(5) += 1

                  var xml = Hbase_get(conn, "human_history", A(i).mac, "subtract", "1")
                  if (xml == null) {
                    xml = "0"
                  }
                  val n = xml.toInt
                  if (n >= 0 && n < 1) t_num(0) += 1
                  else if (n < 2 && n >= 1) t_num(1) += 1
                  else if (n < 3 && n >= 2) t_num(2) += 1
                  else if (n < 4 && n >= 3) t_num(3) += 1
                  else t_num(4) += 1

                  var wz = Hbase_get(conn, "store_time", A(i).mac, "subtract", "1")
                  var wzj = Hbase_get(conn, "store_time", A(i).mac, "intime", "2")
                  if (wzj == null) {
                    wzj = A(i).time
                  }
                  if (wz == null) {
                    wz = "0"
                  }
                  var xpx = wz.toInt
                  val num = Times.Time_Duration_Seconds(A(i).time, wzj)
                  xpx += num.toInt
                  Hbase_insert(conn, "store_time", A(i).mac, "subtract", "1", xpx.toString)
                  Hbase_insert(conn, "store_time", A(i).mac, "intime", "2", A(i).time)
                  //顾客活跃度
                  if (xpx <= 1 * 30 && xpx >= 0) h_num(0) += 1
                  else if (xpx > 1 * 30 && xpx <= 1 * 60) h_num(1) += 1
                  else if (xpx > 1 * 60 && xpx <= 5 * 60) h_num(2) += 1
                  else h_num(3) += 1
                }
              }
              else {
                //在店铺外的人，判断是否是刚出去的
                if (human_in != null) {
                  Hbase_deleteOne(conn, "store_temp", A(i).mac, "time", "1")
                  Hbase_insert(conn, "store_time", A(i).mac, "outtime", "1", A(i).time)
                  //判断新老顾客，记录访问次数和时间
                  /*val values = Hbase_get(conn, "human_times", A(i).mac, "values", "1")

                 if (values == null) {
                   x_num(0) += 1
                   Hbase_insert(conn, "human_times", A(i).mac, "time", "1", human_in)
                   Hbase_insert(conn, "human_times", A(i).mac, "values", "1", "1")
                   Hbase_insert(conn, "human_history", A(i).mac, "this_time", "1", human_in)
                 }
                 else {
                   var x = values.toInt
                   x += 1
                   Hbase_insert(conn, "human_times", A(i).mac, "time", x.toString, human_in)
                   Hbase_insert(conn, "human_times", A(i).mac, "values", "1", x.toString)
                   //来访次数
                   if (x == 2) x_num(1) += 1
                   else if (x == 3) x_num(2) += 1
                   else if (x == 4) x_num(3) += 1
                   else if (x == 5) x_num(4) += 1
                   else x_num(5) += 1

                   Hbase_insert(conn, "human_history", A(i).mac, "last_time", "1", human_last_time)
                   Hbase_insert(conn, "human_history", A(i).mac, "this_time", "1", human_in)
                   val n = Times.Time_Duration_Hours(human_in, human_last_time)
                   Hbase_insert(conn, "human_history", A(i).mac, "subtract", "1", n.toString)
                   //来访周期
                   if (n >= 0 && n < 24) t_num(0) += 1
                   else if (n < 24* 7  && n >= 24) t_num(1) += 1
                   else if (n < 24* 7  *2  && n >= 24* 7) t_num(2) += 1
                   else if (n < 24 *30  && n >= 24* 7  *2) t_num(3) += 1
                   else t_num(4) += 1
                 }*/
                  /*val out = Hbase_get(conn, "human_history", A(i).mac, "subtract", "1")
                    val oo = Hbase_get(conn, "human_times", A(i).mac, "values", "1")
                    if (out != null) {
                      val n = out.toInt
                      val x = oo.toInt
                      if (n >= 0 && n < 24) t_num(0) -= 1
                      else if (n < 24 * 7 && n >= 24) t_num(1) -= 1
                      else if (n < 24 * 7 * 2 && n >= 24 * 7) t_num(2) -= 1
                      else if (n < 24 * 30 && n >= 24 * 7 * 2) t_num(3) -= 1
                      else t_num(4) -= 1

                      if (x == 2) x_num(1) -= 1
                      else if (x == 3) x_num(2) -= 1
                      else if (x == 4) x_num(3) -= 1
                      else if (x == 5) x_num(4) -= 1
                      else x_num(5) += 1
                    }
                    else {
                      x_num(0) -= 1
                    }*/

                  /*Hbase_insert(conn, "store_time", A(i).mac, "intime", "1", human_in)
                    Hbase_insert(conn, "store_time", A(i).mac, "outtime", "1", A(i).time)
                    val num = Times.Time_Duration_Seconds(A(i).time, human_in)
                    Hbase_insert(conn, "store_time", A(i).mac, "subtract", "1", num.toString)
                    //顾客活跃度
                    if (num <= 1 * 30 && num >= 0) h_num(0) += 1
                    else if (num > 1 * 30 && num <= 1 * 60) h_num(1) += 1
                    else if (num > 1 * 60 && num <= 5 * 60) h_num(2) += 1
                    else h_num(3) += 1*/
                }
              }
            }
          }
        }

        Hbase_insert(conn, "count_human_times", A(0).time, "1", "1", x_num(0).toString)
        Hbase_insert(conn, "count_human_times", A(0).time, "2", "1", x_num(1).toString)
        Hbase_insert(conn, "count_human_times", A(0).time, "3", "1", x_num(2).toString)
        Hbase_insert(conn, "count_human_times", A(0).time, "4", "1", x_num(3).toString)
        Hbase_insert(conn, "count_human_times", A(0).time, "5", "1", x_num(4).toString)
        Hbase_insert(conn, "count_human_times", A(0).time, "other", "1", x_num(5).toString)

        Hbase_insert(conn, "shop_times", A(0).time, "1", "1", h_num(0).toString)
        Hbase_insert(conn, "shop_times", A(0).time, "5", "1", h_num(1).toString)
        Hbase_insert(conn, "shop_times", A(0).time, "10", "1", h_num(2).toString)
        Hbase_insert(conn, "shop_times", A(0).time, "15", "1", h_num(3).toString)
        Hbase_insert(conn, "shop_times", A(0).time, "30", "1", h_num(4).toString)
        Hbase_insert(conn, "shop_times", A(0).time, "60", "1", h_num(5).toString)
        Hbase_insert(conn, "shop_times", A(0).time, "other", "1", h_num(6).toString)

        Hbase_insert(conn, "zhudian1", "1", "now", "1", h_num(0).toString)
        Hbase_insert(conn, "zhudian2", "1", "now", "1", h_num(1).toString)
        Hbase_insert(conn, "zhudian3", "1", "now", "1", h_num(2).toString)
        Hbase_insert(conn, "zhudian4", "1", "now", "1", h_num(3).toString)

        Hbase_insert(conn, "interview_times", A(0).time, "12", "1", t_num(0).toString)
        Hbase_insert(conn, "interview_times", A(0).time, "24", "1", t_num(1).toString)
        Hbase_insert(conn, "interview_times", A(0).time, "36", "1", t_num(2).toString)
        Hbase_insert(conn, "interview_times", A(0).time, "48", "1", t_num(3).toString)
        Hbase_insert(conn, "interview_times", A(0).time, "60", "1", t_num(4).toString)

        Hbase_insert(conn, "zhouqi1", "1", "now", "1", t_num(0).toString)
        Hbase_insert(conn, "zhouqi2", "1", "now", "1", t_num(1).toString)
        Hbase_insert(conn, "zhouqi3", "1", "now", "1", t_num(2).toString)
        Hbase_insert(conn, "zhouqi4", "1", "now", "1", t_num(3).toString)
        Hbase_insert(conn, "zhouqi5", "1", "now", "1", t_num(4).toString)




        //计算入店量和入店率
        val count_in = Hbase_count("store_temp")
        Hbase_insert(conn, "store_log", A(0).time, "count", "1", count_in.toString)
        xxx10("rudian", "now", count_in.toString)
        Hbase_insert(conn, "store_log", A(0).time, "rate", "1", (count_in.toDouble / count_all.toDouble).toString)
        xxx10("rudr", "now", (count_in.toDouble / count_all.toDouble).toString)

        Hbase_insert(conn, "zhouqi1", "1", "rate", "1", (t_num(0).toDouble / count_in.toDouble).toString)

        Hbase_insert(conn, "zhouqi2", "1", "rate", "1", (t_num(1).toDouble / count_in.toDouble).toString)

        Hbase_insert(conn, "zhouqi3", "1", "rate", "1", (t_num(2).toDouble / count_in.toDouble).toString)

        Hbase_insert(conn, "zhouqi4", "1", "rate", "1", (t_num(3).toDouble / count_in.toDouble).toString)

        Hbase_insert(conn, "zhouqi5", "1", "rate", "1", (t_num(4).toDouble / count_in.toDouble).toString)
        //当前店铺里的新老顾客数
        /* B = Hbase_scan1(conn, "store_temp")
           new_people= 0
           old_people = 0
           for (i <- 0 until B.length by 1) {
             val human_in = Hbase_get(conn, "human_history", B(i).mac, "this_time", "1")
             if (human_in == null) {
               new_people += 1
             }
             else {
               old_people += 1
             }
           }
           if(B.length!=0) {
             Hbase_insert(conn, "new_old_history", B(0).time, "new", "1", new_people.toString)
             Hbase_insert(conn, "new_old_history", B(0).time, "old", "1", old_people.toString)
             Hbase_insert(conn, "new_old_history", B(0).time, "rate", "1", (new_people.toDouble / old_people.toDouble).toString)
           }*/
        Hbase_insert(conn, "new_old_history", A(0).time, "new", "1", x_num(0).toString)
        Hbase_insert(conn, "new_old_history", A(0).time, "old", "1", (count_in - x_num(0)).toString)
        Hbase_insert(conn, "new_old_history", A(0).time, "rate", "1", (x_num(0).toDouble / (count_in - x_num(0)).toDouble).toString)
        Hbase_insert(conn, "new_old", "1", "new", "1", x_num(0).toString)
        Hbase_insert(conn, "new_old", "1", "old", "1", (count_in - x_num(0)).toString)

        Hbase_insert(conn, "jump_log", A(0).time, "jump_human", "1", h_num(0).toString)
        Hbase_insert(conn, "jump_log", A(0).time, "all_human", "1", count_all.toString)
        Hbase_insert(conn, "jump_log", A(0).time, "rate", "1", (h_num(0).toDouble / count_all.toDouble).toString)

        Hbase_insert(conn, "deep_log", A(0).time, "num", "1", (h_num(5) + h_num(6)).toString)
        Hbase_insert(conn, "deep_log", A(0).time, "all", "1", count_all.toString)
        Hbase_insert(conn, "deep_log", A(0).time, "rate", "1", ((h_num(5) + h_num(6)).toDouble / count_all.toDouble).toString)

        Hbase_insert(conn, "activity_log", A(0).time, "high", "1", (t_num(0) + t_num(1)).toString)
        Hbase_insert(conn, "activity_log", A(0).time, "medium", "1", (t_num(2) + t_num(3)).toString)
        Hbase_insert(conn, "activity_log", A(0).time, "low", "1", (t_num(4) + t_num(5)).toString)
        Hbase_insert(conn, "activity_log", A(0).time, "sleep", "1", t_num(6).toString)

        if (!flag_first) {
          flag_first = true
          flag_xx = A(0).time
          last_hour = A(0).time
          last_day = 2 //24
          last_week = 3 //24*7
          last_month = 4 //24*30
        }

        //记录历史(前一小时、一天、一周、一月）\环比（时、天、周、月）
        if (Times.Time_Duration_Minutes(A(0).time, last_hour) >= 1) {
          last_hour = A(0).time
          val HH = df2.format(new Date())
          val EEE = df_week.format(new Date())
          val MMM = df_month.format(new Date())
          val DDD = df_day.format(new Date())
          var eee:Int=0
          EEE match{
            case "Mon" =>eee=1
            case "Tues" =>eee=2
            case "Wed" =>eee=3
            case "Thur" =>eee=4
            case "Fri" =>eee=5
            case "Sat" =>eee=6
            case "Sun" =>eee=7
          }
          var kss:Int=0
          if(eee==7)
            kss=1
          else kss=eee+1
          var khh:Int=0
          if(HH.toInt==23)
            khh=0
          else khh=HH.toInt+1
          var kmm:Int=0
          if(MMM.toInt==12)
            kmm=1
          else kmm=MMM.toInt+1
          var kdd:Int=0
          if(DDD.toInt==30)
            kdd=1
          else kdd=DDD.toInt+1

          //准备训练集合
          val out_fli1 = new FileWriter("/home/hadoop/Documents/keliu_hour",true)
          out_fli1.write(HH+","+ke_hour.toString+"\n")
          out_fli1.close()
          val raw_data=sc.textFile("/home/hadoop/Documents/keliu_hour")
          val map_data=raw_data.map{x=>
            val split_list=x.split(",")
            (split_list(0).toInt,split_list(1).toInt)
          }
          val dfx=sqcX.createDataFrame(map_data)
          val datax = dfx.toDF("hour" ,"keliu")
          val colArray = Array("hour")
          val assembler = new VectorAssembler().setInputCols(colArray).setOutputCol("features")
          val vecDF: DataFrame = assembler.transform(datax)
          val out_fli2 = new FileWriter("/home/hadoop/Documents/keliu_hour_pr")
          out_fli2.write(khh.toString+",0\n")
          out_fli2.close()
          val raw_data_predict=sc.textFile("/home/hadoop/Documents/keliu_hour_pr")
          val map_data_for_predict=raw_data_predict.map{x=>
            val split_list=x.split(",")
            (split_list(0).toInt,split_list(1).toInt)
          }
          val df_for_predict=sqcX.createDataFrame(map_data_for_predict)
          val data_for_predict = df_for_predict.toDF("hour" ,"keliu")
          val colArray_for_predict = Array("hour")
          val assembler_for_predict = new VectorAssembler().setInputCols(colArray_for_predict).setOutputCol("features")
          val vecDF_for_predict: DataFrame = assembler_for_predict.transform(data_for_predict)
          // 建立模型，预测keliu
          // 设置线性回归参数
          val lr13 = new LinearRegression()
          val lr2 = lr13.setFeaturesCol("features").setLabelCol("keliu").setFitIntercept(true)
          // RegParam：正则化
          val lr3 = lr2.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
          val lr = lr3
          // 将训练集合代入模型进行训练
          val lrModel = lr.fit(vecDF)
          // 输出模型全部参数
          lrModel.extractParamMap()
          val predictions: DataFrame = lrModel.transform(vecDF_for_predict)
          //println("输出预测结果")
          val predict_result: DataFrame =predictions.selectExpr("prediction")
          var listRow= predict_result.rdd.collect()
          var ms=listRow(0).toString()
          var redict_re=ms.slice(1,ms.length-1)
          //println(redict_re.toDouble.toInt)
          Hbase_insert(conn,"keliu_pr","1","hour","1",redict_re.toDouble.toInt.toString)

          //准备训练集合
          val out_fli11 = new FileWriter("/home/hadoop/Documents/rudian_hour",true)
          out_fli11.write(HH+","+ru_hour.toString+"\n")
          out_fli11.close()
          val raw_data1=sc.textFile("/home/hadoop/Documents/rudian_hour")
          val map_data1=raw_data1.map{x=>
            val split_list=x.split(",")
            (split_list(0).toInt,split_list(1).toInt)
          }
          val dfx1=sqcX.createDataFrame(map_data)
          val datax1 = dfx1.toDF("hour" ,"rudian")
          val colArray1 = Array("hour")
          val assembler1 = new VectorAssembler().setInputCols(colArray1).setOutputCol("features")
          val vecDF1: DataFrame = assembler1.transform(datax1)
          val out_fli21 = new FileWriter("/home/hadoop/Documents/rudian_hour_pr")
          out_fli21.write(khh.toString+",0\n")
          out_fli21.close()
          val raw_data_predict1=sc.textFile("/home/hadoop/Documents/rudian_hour_pr")
          val map_data_for_predict1=raw_data_predict1.map{x=>
            val split_list=x.split(",")
            (split_list(0).toInt,split_list(1).toInt)
          }
          val df_for_predict1=sqcX.createDataFrame(map_data_for_predict)
          val data_for_predict1 = df_for_predict1.toDF("hour" ,"rudian")
          val colArray_for_predict1 = Array("hour")
          val assembler_for_predict1 = new VectorAssembler().setInputCols(colArray_for_predict1).setOutputCol("features")
          val vecDF_for_predict1: DataFrame = assembler_for_predict1.transform(data_for_predict1)
          // 建立模型，预测keliu
          // 设置线性回归参数
          val lr11 = new LinearRegression()
          val lr21 = lr11.setFeaturesCol("features").setLabelCol("rudian").setFitIntercept(true)
          // RegParam：正则化
          val lr31 = lr21.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
          val lr1 = lr31
          // 将训练集合代入模型进行训练
          val lrModel1 = lr1.fit(vecDF1)
          // 输出模型全部参数
          lrModel1.extractParamMap()
          val predictions1: DataFrame = lrModel1.transform(vecDF_for_predict1)
          //println("输出预测结果")
          val predict_result1: DataFrame =predictions1.selectExpr("prediction")
          var listRow1= predict_result1.rdd.collect()
          var ms1=listRow1(0).toString()
          var redict_re1=ms1.slice(1,ms1.length-1)
          //println(redict_re.toDouble.toInt)
          Hbase_insert(conn,"rudian_pr","1","hour","1",redict_re1.toDouble.toInt.toString)
          Hbase_insert(conn,"rudr_pr","1","hour","1",((redict_re.toDouble.toInt.toDouble-redict_re1.toDouble.toInt.toDouble)/redict_re.toDouble.toInt.toDouble).toString)


          Hbase_insert(conn, "day_keliu", "1", "hour", HH, ke_hour.toString)
          var hx = Hbase_get(conn, "keliu", "1", "hour", "2")
          if (hx != null) {
            Hbase_insert(conn, "keliu", "1", "hour", "3", hx)
          }
          var hx2 = Hbase_get(conn, "keliu", "1", "hour", "1")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (ke_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "keliu", "1", "h_hour", "1", x.toString)
            Hbase_insert(conn, "keliu", "1", "hour", "2", hx2)
          }
          Hbase_insert(conn, "keliu", "1", "hour", "1", ke_hour.toString)

          hx = Hbase_get(conn, "rudian", "1", "hour", "2")
          if (hx != null) {
            Hbase_insert(conn, "rudian", "1", "hour", "3", hx)
          }
          hx2 = Hbase_get(conn, "rudian", "1", "hour", "1")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (ru_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "rudian", "1", "h_hour", "1", x.toString)
            Hbase_insert(conn, "rudian", "1", "hour", "2", hx2)
          }
          Hbase_insert(conn, "rudian", "1", "hour", "1", ru_hour.toString)

          rur_hour = ru_hour.toDouble / ke_hour.toDouble
          hx = Hbase_get(conn, "rudr", "1", "hour", "2")
          if (hx != null) {
            Hbase_insert(conn, "rudr", "1", "hour", "3", hx)
          }
          hx2 = Hbase_get(conn, "rudr", "1", "hour", "1")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (rur_hour - hx2.toDouble) / hx2.toDouble
            Hbase_insert(conn, "rudr", "1", "h_hour", "1", x.toString)
            Hbase_insert(conn, "rudr", "1", "hour", "2", hx2)
          }
          Hbase_insert(conn, "rudr", "1", "hour", "1", rur_hour.toString)

          var xms = new_hour.toDouble / old_hour.toDouble
          hx2 = Hbase_get(conn, "new_old", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (xms - hx2.toDouble) / hx2.toDouble
            Hbase_insert(conn, "new_old", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "new_old", "1", "h_hour", "2", xms.toString)
          xxx("new_old", "hour", xms.toString)
          xxx("zhouqi1", "hour", zq1_hour.toString)
          xxx("zhouqi2", "hour", zq2_hour.toString)
          xxx("zhouqi3", "hour", zq3_hour.toString)
          xxx("zhouqi4", "hour", zq4_hour.toString)
          xxx("zhouqi5", "hour", zq5_hour.toString)
          hx2 = Hbase_get(conn, "zhouqi1", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zq1_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhouqi1", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhouqi1", "1", "h_hour", "2", zq1_hour.toString)

          hx2 = Hbase_get(conn, "zhouqi2", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zq2_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhouqi2", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhouqi2", "1", "h_hour", "2", zq2_hour.toString)
          hx2 = Hbase_get(conn, "zhouqi3", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zq3_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhouqi3", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhouqi3", "1", "h_hour", "2", zq3_hour.toString)
          hx2 = Hbase_get(conn, "zhouqi4", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zq4_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhouqi4", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhouqi4", "1", "h_hour", "2", zq4_hour.toString)
          hx2 = Hbase_get(conn, "zhouqi5", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zq5_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhouqi5", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhouqi5", "1", "h_hour", "2", zq5_hour.toString)

          xxx("zhudian1", "hour", zd1_hour.toString)
          xxx("zhudian2", "hour", zd2_hour.toString)
          xxx("zhudian3", "hour", zd3_hour.toString)
          xxx("zhudian3", "hour", zd4_hour.toString)
          hx2 = Hbase_get(conn, "zhudian1", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zd1_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhudian1", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhudian1", "1", "h_hour", "2", zd1_hour.toString)
          hx2 = Hbase_get(conn, "zhudian2", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zd2_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhudian2", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhudian2", "1", "h_hour", "2", zd2_hour.toString)
          hx2 = Hbase_get(conn, "zhudian3", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zd3_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhudian3", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhudian3", "1", "h_hour", "2", zd3_hour.toString)
          hx2 = Hbase_get(conn, "zhudian4", "1", "h_hour", "2")
          if (hx2 != null && hx2 != 0) {
            val x: Double = (zd4_hour - hx2.toInt).toDouble / hx2.toDouble
            Hbase_insert(conn, "zhudian4", "1", "h_hour", "1", x.toString)
          }
          Hbase_insert(conn, "zhudian4", "1", "h_hour", "2", zd4_hour.toString)

          last_day -= 1
          last_month -= 1
          last_week -= 1
          if (last_day == 0) {
            last_day = 2 //24
            //准备训练集合
            val out_fli1 = new FileWriter("/home/hadoop/Documents/keliu_day",true)
            out_fli1.write(DDD+","+ke_day.toString+"\n")
            out_fli1.close()
            val raw_data=sc.textFile("/home/hadoop/Documents/keliu_day")
            val map_data=raw_data.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val dfx=sqcX.createDataFrame(map_data)
            val datax = dfx.toDF("day" ,"keliu")
            val colArray = Array("day")
            val assembler = new VectorAssembler().setInputCols(colArray).setOutputCol("features")
            val vecDF: DataFrame = assembler.transform(datax)

            val out_fli2 = new FileWriter("/home/hadoop/Documents/keliu_day_pr")
            out_fli2.write(kdd.toString+",0\n")
            out_fli2.close()
            val raw_data_predict=sc.textFile("/home/hadoop/Documents/keliu_day_pr")
            val map_data_for_predict=raw_data_predict.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val df_for_predict=sqcX.createDataFrame(map_data_for_predict)
            val data_for_predict = df_for_predict.toDF("day" ,"keliu")
            val colArray_for_predict = Array("day")
            val assembler_for_predict = new VectorAssembler().setInputCols(colArray_for_predict).setOutputCol("features")
            val vecDF_for_predict: DataFrame = assembler_for_predict.transform(data_for_predict)
            // 建立模型，预测keliu
            // 设置线性回归参数
            val lr13 = new LinearRegression()
            val lr2 = lr13.setFeaturesCol("features").setLabelCol("keliu").setFitIntercept(true)
            // RegParam：正则化
            val lr3 = lr2.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
            val lr = lr3
            // 将训练集合代入模型进行训练
            val lrModel = lr.fit(vecDF)
            // 输出模型全部参数
            lrModel.extractParamMap()
            val predictions: DataFrame = lrModel.transform(vecDF_for_predict)
            //println("输出预测结果")
            val predict_result: DataFrame =predictions.selectExpr("prediction")
            var listRow= predict_result.rdd.collect()
            var ms=listRow(0).toString()
            var redict_re=ms.slice(1,ms.length-1)
            //println(redict_re.toDouble.toInt)
            Hbase_insert(conn,"keliu_pr","1","day","1",redict_re.toDouble.toInt.toString)

            //准备训练集合
            val out_fli11 = new FileWriter("/home/hadoop/Documents/rudian_day",true)
            out_fli11.write(DDD+","+ru_day.toString+"\n")
            out_fli11.close()
            val raw_data1=sc.textFile("/home/hadoop/Documents/rudian_day")
            val map_data1=raw_data1.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val dfx1=sqcX.createDataFrame(map_data)
            val datax1 = dfx1.toDF("day" ,"rudian")
            val colArray1 = Array("day")
            val assembler1 = new VectorAssembler().setInputCols(colArray1).setOutputCol("features")
            val vecDF1: DataFrame = assembler1.transform(datax1)
            val out_fli21 = new FileWriter("/home/hadoop/Documents/rudian_day_pr")
            out_fli21.write(kdd.toString+",0\n")
            out_fli21.close()
            val raw_data_predict1=sc.textFile("/home/hadoop/Documents/rudian_day_pr")
            val map_data_for_predict1=raw_data_predict1.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val df_for_predict1=sqcX.createDataFrame(map_data_for_predict)
            val data_for_predict1 = df_for_predict1.toDF("day" ,"rudian")
            val colArray_for_predict1 = Array("day")
            val assembler_for_predict1 = new VectorAssembler().setInputCols(colArray_for_predict1).setOutputCol("features")
            val vecDF_for_predict1: DataFrame = assembler_for_predict1.transform(data_for_predict1)
            // 建立模型，预测keliu
            // 设置线性回归参数
            val lr11 = new LinearRegression()
            val lr21 = lr11.setFeaturesCol("features").setLabelCol("rudian").setFitIntercept(true)
            // RegParam：正则化
            val lr31 = lr21.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
            val lr1 = lr31
            // 将训练集合代入模型进行训练
            val lrModel1 = lr1.fit(vecDF1)
            // 输出模型全部参数
            lrModel1.extractParamMap()
            val predictions1: DataFrame = lrModel1.transform(vecDF_for_predict1)
            //println("输出预测结果")
            val predict_result1: DataFrame =predictions1.selectExpr("prediction")
            var listRow1= predict_result1.rdd.collect()
            var ms1=listRow1(0).toString()
            var redict_re1=ms1.slice(1,ms1.length-1)
            //println(redict_re.toDouble.toInt)
            Hbase_insert(conn,"rudian_pr","1","day","1",redict_re1.toDouble.toInt.toString)
            Hbase_insert(conn,"rudr_pr","1","day","1",((redict_re.toDouble.toInt.toDouble-redict_re1.toDouble.toInt.toDouble)/redict_re.toDouble.toInt.toDouble).toString)

            var dx = Hbase_get(conn, "keliu", "1", "day", "3")
            if (dx != null) {
              Hbase_insert(conn, "keliu", "1", "day", "4", dx)
            }
            dx = Hbase_get(conn, "keliu", "1", "day", "2")
            if (dx != null) {
              Hbase_insert(conn, "keliu", "1", "day", "3", dx)
            }
            var dx2 = Hbase_get(conn, "keliu", "1", "day", "1")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (ke_day - dx2.toInt).toDouble / dx2.toDouble
              Hbase_insert(conn, "keliu", "1", "h_day", "1", x.toString)
              Hbase_insert(conn, "keliu", "1", "day", "2", dx2)
            }
            Hbase_insert(conn, "keliu", "1", "day", "1", ke_day.toString)

            dx = Hbase_get(conn, "rudian", "1", "day", "3")
            if (dx != null) {
              Hbase_insert(conn, "rudian", "1", "day", "4", dx)
            }
            dx = Hbase_get(conn, "rudian", "1", "day", "2")
            if (dx != null) {
              Hbase_insert(conn, "rudian", "1", "day", "3", dx)
            }
            dx2 = Hbase_get(conn, "rudian", "1", "day", "1")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (ru_day - dx2.toInt).toDouble / dx2.toDouble
              Hbase_insert(conn, "rudian", "1", "h_day", "1", x.toString)
              Hbase_insert(conn, "rudian", "1", "day", "2", dx2)
            }
            Hbase_insert(conn, "rudian", "1", "day", "1", ru_day.toString)

            rur_day = ru_day.toDouble / ke_day.toDouble
            dx = Hbase_get(conn, "rudr", "1", "day", "3")
            if (dx != null) {
              Hbase_insert(conn, "rudr", "1", "day", "4", dx)
            }
            dx = Hbase_get(conn, "rudr", "1", "day", "2")
            if (dx != null) {
              Hbase_insert(conn, "rudr", "1", "day", "3", dx)
            }
            dx2 = Hbase_get(conn, "rudr", "1", "day", "1")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (rur_day - dx2.toDouble) / dx2.toDouble
              Hbase_insert(conn, "rudr", "1", "h_day", "1", x.toString)
              Hbase_insert(conn, "rudr", "1", "day", "2", dx2)
            }
            Hbase_insert(conn, "rudr", "1", "day", "1", rur_day.toString)

            xxx("zhouqi1", "day", zq1_day.toString)
            xxx("zhouqi2", "day", zq2_day.toString)
            xxx("zhouqi3", "day", zq3_day.toString)
            xxx("zhouqi4", "day", zq4_day.toString)
            xxx("zhouqi5", "day", zq5_day.toString)

            dx2 = Hbase_get(conn, "zhouqi1", "1", "h_day", "2")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (zq1_day - dx2.toInt).toDouble / dx2.toDouble
              Hbase_insert(conn, "zhouqi1", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi1", "1", "h_day", "2", zq1_day.toString)
            dx2 = Hbase_get(conn, "zhouqi2", "1", "h_day", "2")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (zq2_day - dx2.toInt).toDouble / dx2.toDouble
              Hbase_insert(conn, "zhouqi2", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi2", "1", "h_day", "2", zq2_day.toString)
            dx2 = Hbase_get(conn, "zhouqi3", "1", "h_day", "2")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (zq3_day - dx2.toInt).toDouble / dx2.toDouble
              Hbase_insert(conn, "zhouqi3", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi3", "1", "h_day", "2", zq3_day.toString)
            dx2 = Hbase_get(conn, "zhouqi4", "1", "h_day", "2")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (zq4_day - dx2.toInt).toDouble / dx2.toDouble
              Hbase_insert(conn, "zhouqi4", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi4", "1", "h_day", "2", zq4_day.toString)
            dx2 = Hbase_get(conn, "zhouqi5", "1", "h_day", "2")
            if (dx2 != null && dx2 != 0) {
              val x: Double = (zq5_day - dx2.toInt).toDouble / dx2.toDouble
              Hbase_insert(conn, "zhouqi5", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi5", "1", "h_day", "2", zq5_day.toString)

            xms = new_day.toDouble / old_day.toDouble
            hx2 = Hbase_get(conn, "new_old", "1", "h_day", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (xms - hx2.toDouble) / hx2.toDouble
              Hbase_insert(conn, "new_old", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "new_old", "1", "h_day", "2", xms.toString)
            xxx("new_old", "day", xms.toString)

            xxx("zhudian1", "day", zd1_day.toString)
            xxx("zhudian2", "day", zd2_day.toString)
            xxx("zhudian3", "day", zd3_day.toString)
            xxx("zhudian3", "day", zd4_day.toString)
            hx2 = Hbase_get(conn, "zhudian1", "1", "h_day", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd1_day - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian1", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian1", "1", "h_day", "2", zd1_day.toString)
            hx2 = Hbase_get(conn, "zhudian2", "1", "h_day", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd2_day - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian2", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian2", "1", "h_day", "2", zd2_day.toString)
            hx2 = Hbase_get(conn, "zhudian3", "1", "h_day", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd3_day - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian3", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian3", "1", "h_day", "2", zd3_day.toString)
            hx2 = Hbase_get(conn, "zhudian4", "1", "h_day", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd4_day - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian4", "1", "h_day", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian4", "1", "h_day", "2", zd4_day.toString)

            ru_day = 0
            ke_day = 0
            zq1_day = 0
            zq2_day = 0
            zq3_day = 0
            zq4_day = 0
            zq5_day = 0
            new_day = 0
            old_day = 0
            zd1_day = 0
            zd2_day = 0
            zd3_day = 0
            zd4_day = 0
          }
          else {
            ke_day += ke_hour
            ru_day += ru_hour
            zq1_day += zq1_hour
            zq2_day += zq2_hour
            zq3_day += zq3_hour
            zq4_day += zq4_hour
            zq5_day += zq5_hour
            new_day += new_hour
            old_day += old_hour
            zd1_day += zd1_hour
            zd2_day += zd2_hour
            zd3_day += zd3_hour
            zd4_day += zd4_hour
          }

          if (last_week == 0) {
            last_week = 3
            //24*7
            //准备训练集合
            val out_fli1 = new FileWriter("/home/hadoop/Documents/keliu_week",true)
            out_fli1.write(eee+","+kss.toString+"\n")
            out_fli1.close()
            val raw_data=sc.textFile("/home/hadoop/Documents/keliu_week")
            val map_data=raw_data.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val dfx=sqcX.createDataFrame(map_data)
            val datax = dfx.toDF("week" ,"keliu")
            val colArray = Array("week")
            val assembler = new VectorAssembler().setInputCols(colArray).setOutputCol("features")
            val vecDF: DataFrame = assembler.transform(datax)
            val out_fli2 = new FileWriter("/home/hadoop/Documents/keliu_week_pr")
            out_fli2.write(kss.toString+",0\n")
            out_fli2.close()
            val raw_data_predict=sc.textFile("/home/hadoop/Documents/keliu_week_pr")
            val map_data_for_predict=raw_data_predict.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val df_for_predict=sqcX.createDataFrame(map_data_for_predict)
            val data_for_predict = df_for_predict.toDF("week" ,"keliu")
            val colArray_for_predict = Array("week")
            val assembler_for_predict = new VectorAssembler().setInputCols(colArray_for_predict).setOutputCol("features")
            val vecDF_for_predict: DataFrame = assembler_for_predict.transform(data_for_predict)
            // 建立模型，预测keliu
            // 设置线性回归参数
            val lr13 = new LinearRegression()
            val lr2 = lr13.setFeaturesCol("features").setLabelCol("keliu").setFitIntercept(true)
            // RegParam：正则化
            val lr3 = lr2.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
            val lr = lr3
            // 将训练集合代入模型进行训练
            val lrModel = lr.fit(vecDF)
            // 输出模型全部参数
            lrModel.extractParamMap()
            val predictions: DataFrame = lrModel.transform(vecDF_for_predict)
            //println("输出预测结果")
            val predict_result: DataFrame =predictions.selectExpr("prediction")
            var listRow= predict_result.rdd.collect()
            var ms=listRow(0).toString()
            var redict_re=ms.slice(1,ms.length-1)
            //println(redict_re.toDouble.toInt)
            Hbase_insert(conn,"keliu_pr","1","week","1",redict_re.toDouble.toInt.toString)

            val raw_data_predict3=sc.textFile("/home/hadoop/Documents/week_pr")
            val map_data_for_predict3=raw_data_predict3.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val df_for_predict3=sqcX.createDataFrame(map_data_for_predict3)
            val data_for_predict3 = df_for_predict3.toDF("week" ,"keliu")
            val colArray_for_predict3 = Array("week")
            val assembler_for_predict3 = new VectorAssembler().setInputCols(colArray_for_predict3).setOutputCol("features")
            val vecDF_for_predict3: DataFrame = assembler_for_predict3.transform(data_for_predict3)
            val predictions3: DataFrame = lrModel.transform(vecDF_for_predict3)
            val predict_result3: DataFrame =predictions3.selectExpr("prediction")
            var listRow3= predict_result3.rdd.collect()
            var ms3=listRow3(0).toString()
            var redict_re3=ms3.slice(1,ms3.length-1)
            Hbase_insert(conn,"week_pr","1","values","1",ms3)
            ms3=listRow3(1).toString()
            redict_re3=ms3.slice(1,ms3.length-1)
            Hbase_insert(conn,"week_pr","1","values","2",ms3)
            ms3=listRow3(2).toString()
            redict_re3=ms3.slice(1,ms3.length-1)
            Hbase_insert(conn,"week_pr","1","values","3",ms3)
            ms3=listRow3(3).toString()
            redict_re3=ms3.slice(1,ms3.length-1)
            Hbase_insert(conn,"week_pr","1","values","4",ms3)
            ms3=listRow3(4).toString()
            redict_re3=ms3.slice(1,ms3.length-1)
            Hbase_insert(conn,"week_pr","1","values","5",ms3)
            ms3=listRow3(5).toString()
            redict_re3=ms3.slice(1,ms3.length-1)
            Hbase_insert(conn,"week_pr","1","values","6",ms3)
            ms3=listRow3(6).toString()
            redict_re3=ms3.slice(1,ms3.length-1)
            Hbase_insert(conn,"week_pr","1","values","7",ms3)

            //准备训练集合
            val out_fli11 = new FileWriter("/home/hadoop/Documents/rudian_week",true)
            out_fli11.write(eee+","+ru_week.toString+"\n")
            out_fli11.close()
            val raw_data1=sc.textFile("/home/hadoop/Documents/rudian_week")
            val map_data1=raw_data1.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val dfx1=sqcX.createDataFrame(map_data)
            val datax1 = dfx1.toDF("week" ,"rudian")
            val colArray1 = Array("week")
            val assembler1 = new VectorAssembler().setInputCols(colArray1).setOutputCol("features")
            val vecDF1: DataFrame = assembler1.transform(datax1)
            val out_fli21 = new FileWriter("/home/hadoop/Documents/rudian_week_pr")
            out_fli21.write(kss.toString+",0\n")
            out_fli21.close()
            val raw_data_predict1=sc.textFile("/home/hadoop/Documents/rudian_week_pr")
            val map_data_for_predict1=raw_data_predict1.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val df_for_predict1=sqcX.createDataFrame(map_data_for_predict)
            val data_for_predict1 = df_for_predict1.toDF("week" ,"rudian")
            val colArray_for_predict1 = Array("week")
            val assembler_for_predict1 = new VectorAssembler().setInputCols(colArray_for_predict1).setOutputCol("features")
            val vecDF_for_predict1: DataFrame = assembler_for_predict1.transform(data_for_predict1)
            // 建立模型，预测keliu
            // 设置线性回归参数
            val lr11 = new LinearRegression()
            val lr21 = lr11.setFeaturesCol("features").setLabelCol("rudian").setFitIntercept(true)
            // RegParam：正则化
            val lr31 = lr21.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
            val lr1 = lr31
            // 将训练集合代入模型进行训练
            val lrModel1 = lr1.fit(vecDF1)
            // 输出模型全部参数
            lrModel1.extractParamMap()
            val predictions1: DataFrame = lrModel1.transform(vecDF_for_predict1)
            //println("输出预测结果")
            val predict_result1: DataFrame =predictions1.selectExpr("prediction")
            var listRow1= predict_result1.rdd.collect()
            var ms1=listRow1(0).toString()
            var redict_re1=ms1.slice(1,ms1.length-1)
            //println(redict_re.toDouble.toInt)
            Hbase_insert(conn,"rudian_pr","1","week","1",redict_re1.toDouble.toInt.toString)
            Hbase_insert(conn,"rudr_pr","1","week","1",((redict_re.toDouble.toInt.toDouble-redict_re1.toDouble.toInt.toDouble)/redict_re.toDouble.toInt.toDouble).toString)


            var wx = Hbase_get(conn, "keliu", "1", "week", "3")
            if (wx != null) {
              Hbase_insert(conn, "keliu", "1", "week", "4", wx)
            }
            var wx2 = Hbase_get(conn, "keliu", "1", "week", "2")
            if (wx2 != null) {
              Hbase_insert(conn, "keliu", "1", "week", "3", wx2)
            }
            var wx3 = Hbase_get(conn, "keliu", "1", "week", "1")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (ke_week - wx3.toInt).toDouble / wx3.toDouble
              Hbase_insert(conn, "keliu", "1", "h_week", "1", x.toString)
              Hbase_insert(conn, "keliu", "1", "week", "2", wx3)
            }
            Hbase_insert(conn, "keliu", "1", "week", "1", ke_week.toString)

            wx = Hbase_get(conn, "rudian", "1", "week", "3")
            if (wx != null) {
              Hbase_insert(conn, "rudian", "1", "week", "4", wx)
            }
            wx2 = Hbase_get(conn, "rudian", "1", "week", "2")
            if (wx2 != null) {
              Hbase_insert(conn, "rudian", "1", "week", "3", wx2)
            }
            wx3 = Hbase_get(conn, "rudian", "1", "week", "1")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (ru_week - wx3.toInt).toDouble / wx3.toDouble
              Hbase_insert(conn, "rudian", "1", "h_week", "1", x.toString)
              Hbase_insert(conn, "rudian", "1", "week", "2", wx3)
            }
            Hbase_insert(conn, "rudian", "1", "week", "1", ru_week.toString)

            rur_week = ru_week.toDouble / ke_week.toDouble
            wx = Hbase_get(conn, "rudr", "1", "week", "3")
            if (wx != null) {
              Hbase_insert(conn, "rudr", "1", "week", "4", wx)
            }
            wx2 = Hbase_get(conn, "rudr", "1", "week", "2")
            if (wx2 != null) {
              Hbase_insert(conn, "rudr", "1", "week", "3", wx2)
            }
            wx3 = Hbase_get(conn, "rudr", "1", "week", "1")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (rur_week - wx3.toDouble) / wx3.toDouble
              Hbase_insert(conn, "rudr", "1", "h_week", "1", x.toString)
              Hbase_insert(conn, "rudr", "1", "week", "2", wx3)
            }
            Hbase_insert(conn, "rudr", "1", "week", "1", rur_week.toString)
            xxx("zhouqi1", "week", zq1_week.toString)
            xxx("zhouqi2", "week", zq2_week.toString)
            xxx("zhouqi3", "week", zq3_week.toString)
            xxx("zhouqi4", "week", zq4_week.toString)
            xxx("zhouqi5", "week", zq5_week.toString)
            wx3 = Hbase_get(conn, "zhouqi1", "1", "h_week", "2")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (zq1_week - wx3.toDouble) / wx3.toDouble
              Hbase_insert(conn, "zhouqi1", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi1", "1", "h_week", "2", zq1_week.toString)
            wx3 = Hbase_get(conn, "zhouqi2", "1", "h_week", "2")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (zq2_week - wx3.toDouble) / wx3.toDouble
              Hbase_insert(conn, "zhouqi2", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi2", "1", "h_week", "2", zq2_week.toString)
            wx3 = Hbase_get(conn, "zhouqi3", "1", "h_week", "2")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (zq3_week - wx3.toDouble) / wx3.toDouble
              Hbase_insert(conn, "zhouqi3", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi3", "1", "h_week", "2", zq3_week.toString)
            wx3 = Hbase_get(conn, "zhouqi4", "1", "h_week", "2")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (zq4_week - wx3.toDouble) / wx3.toDouble
              Hbase_insert(conn, "zhouqi4", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi4", "1", "h_week", "2", zq4_week.toString)
            wx3 = Hbase_get(conn, "zhouqi5", "1", "h_week", "2")
            if (wx3 != null && wx3 != 0) {
              val x: Double = (zq5_week - wx3.toDouble) / wx3.toDouble
              Hbase_insert(conn, "zhouqi5", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi5", "1", "h_week", "2", zq5_week.toString)

            xms = new_week.toDouble / old_week.toDouble
            hx2 = Hbase_get(conn, "new_old", "1", "h_week", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (xms - hx2.toDouble) / hx2.toDouble
              Hbase_insert(conn, "new_old", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "new_old", "1", "h_week", "2", xms.toString)
            xxx("new_old", "week", xms.toString)

            xxx("zhudian1", "week", zd1_week.toString)
            xxx("zhudian2", "week", zd2_week.toString)
            xxx("zhudian3", "week", zd3_week.toString)
            xxx("zhudian3", "week", zd4_week.toString)
            hx2 = Hbase_get(conn, "zhudian1", "1", "h_week", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd1_week - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian1", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian1", "1", "h_week", "2", zd1_week.toString)
            hx2 = Hbase_get(conn, "zhudian2", "1", "h_week", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd2_week - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian2", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian2", "1", "h_week", "2", zd2_week.toString)
            hx2 = Hbase_get(conn, "zhudian3", "1", "h_week", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd3_week - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian3", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian3", "1", "h_week", "2", zd3_week.toString)
            hx2 = Hbase_get(conn, "zhudian4", "1", "h_week", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd4_week - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian4", "1", "h_week", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian4", "1", "h_week", "2", zd4_week.toString)

            zd1_week = 0
            zd2_week = 0
            zd3_week = 0
            zd4_week = 0
            ru_week = 0
            ke_week = 0
            zq1_week = 0
            zq2_week = 0
            zq3_week = 0
            zq4_week = 0
            zq5_week = 0
            new_week = 0
            old_week = 0
          }
          else {
            ke_week += ke_hour
            ru_week += ru_hour
            zq1_week += zq1_hour
            zq2_week += zq2_hour
            zq3_week += zq3_hour
            zq5_week += zq5_hour
            zq4_week += zq4_hour
            new_week += new_hour
            old_week += old_hour
            zd1_week += zd1_hour
            zd2_week += zd2_hour
            zd3_week += zd3_hour
            zd4_week += zd4_hour
          }

          if (last_month == 0) {
            last_month = 4
            //24*30
            //准备训练集合
            val out_fli1 = new FileWriter("/home/hadoop/Documents/keliu_month",true)
            out_fli1.write(MMM+","+ke_month.toString+"\n")
            out_fli1.close()
            val raw_data=sc.textFile("/home/hadoop/Documents/keliu_month")
            val map_data=raw_data.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val dfx=sqcX.createDataFrame(map_data)
            val datax = dfx.toDF("month" ,"keliu")
            val colArray = Array("month")
            val assembler = new VectorAssembler().setInputCols(colArray).setOutputCol("features")
            val vecDF: DataFrame = assembler.transform(datax)
            val out_fli2 = new FileWriter("/home/hadoop/Documents/keliu_month_pr")
            out_fli2.write(kmm.toString+",0\n")
            out_fli2.close()
            val raw_data_predict=sc.textFile("/home/hadoop/Documents/keliu_month_pr")
            val map_data_for_predict=raw_data_predict.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val df_for_predict=sqcX.createDataFrame(map_data_for_predict)
            val data_for_predict = df_for_predict.toDF("month" ,"keliu")
            val colArray_for_predict = Array("month")
            val assembler_for_predict = new VectorAssembler().setInputCols(colArray_for_predict).setOutputCol("features")
            val vecDF_for_predict: DataFrame = assembler_for_predict.transform(data_for_predict)
            // 建立模型，预测keliu
            // 设置线性回归参数
            val lr13 = new LinearRegression()
            val lr2 = lr13.setFeaturesCol("features").setLabelCol("keliu").setFitIntercept(true)
            // RegParam：正则化
            val lr3 = lr2.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
            val lr = lr3
            // 将训练集合代入模型进行训练
            val lrModel = lr.fit(vecDF)
            // 输出模型全部参数
            lrModel.extractParamMap()
            val predictions: DataFrame = lrModel.transform(vecDF_for_predict)
            //println("输出预测结果")
            val predict_result: DataFrame =predictions.selectExpr("prediction")
            var listRow= predict_result.rdd.collect()
            var ms=listRow(0).toString()
            var redict_re=ms.slice(1,ms.length-1)
            //println(redict_re.toDouble.toInt)
            Hbase_insert(conn,"keliu_pr","1","month","1",redict_re.toDouble.toInt.toString)

            //准备训练集合
            val out_fli11 = new FileWriter("/home/hadoop/Documents/rudian_month",true)
            out_fli11.write(MMM+","+ru_month.toString+"\n")
            out_fli11.close()
            val raw_data1=sc.textFile("/home/hadoop/Documents/rudian_month")
            val map_data1=raw_data1.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val dfx1=sqcX.createDataFrame(map_data)
            val datax1 = dfx1.toDF("month" ,"rudian")
            val colArray1 = Array("month")
            val assembler1 = new VectorAssembler().setInputCols(colArray1).setOutputCol("features")
            val vecDF1: DataFrame = assembler1.transform(datax1)
            val out_fli21 = new FileWriter("/home/hadoop/Documents/rudian_month_pr")
            out_fli21.write(kmm.toString+",0\n")
            out_fli21.close()
            val raw_data_predict1=sc.textFile("/home/hadoop/Documents/rudian_month_pr")
            val map_data_for_predict1=raw_data_predict1.map{x=>
              val split_list=x.split(",")
              (split_list(0).toInt,split_list(1).toInt)
            }
            val df_for_predict1=sqcX.createDataFrame(map_data_for_predict)
            val data_for_predict1 = df_for_predict1.toDF("month" ,"rudian")
            val colArray_for_predict1 = Array("month")
            val assembler_for_predict1 = new VectorAssembler().setInputCols(colArray_for_predict1).setOutputCol("features")
            val vecDF_for_predict1: DataFrame = assembler_for_predict1.transform(data_for_predict1)
            // 建立模型，预测keliu
            // 设置线性回归参数
            val lr11 = new LinearRegression()
            val lr21 = lr11.setFeaturesCol("features").setLabelCol("rudian").setFitIntercept(true)
            // RegParam：正则化
            val lr31 = lr21.setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)
            val lr1 = lr31
            // 将训练集合代入模型进行训练
            val lrModel1 = lr1.fit(vecDF1)
            // 输出模型全部参数
            lrModel1.extractParamMap()
            val predictions1: DataFrame = lrModel1.transform(vecDF_for_predict1)
            //println("输出预测结果")
            val predict_result1: DataFrame =predictions1.selectExpr("prediction")
            var listRow1= predict_result1.rdd.collect()
            var ms1=listRow1(0).toString()
            var redict_re1=ms1.slice(1,ms1.length-1)
            //println(redict_re.toDouble.toInt)
            Hbase_insert(conn,"rudian_pr","1","month","1",redict_re1.toDouble.toInt.toString)
            Hbase_insert(conn,"rudr_pr","1","month","1",((redict_re.toDouble.toInt.toDouble-redict_re1.toDouble.toInt.toDouble)/redict_re.toDouble.toInt.toDouble).toString)


            var mx = Hbase_get(conn, "keliu", "1", "month", "2")
            if (mx != null) {
              Hbase_insert(conn, "keliu", "1", "month", "3", mx)
            }
            var mx2 = Hbase_get(conn, "keliu", "1", "month", "1")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (ke_month - mx2.toInt).toDouble / mx2.toDouble
              Hbase_insert(conn, "keliu", "1", "h_month", "1", x.toString)
              Hbase_insert(conn, "keliu", "1", "month", "2", mx2)
            }
            Hbase_insert(conn, "keliu", "1", "month", "1", ke_month.toString)

            mx = Hbase_get(conn, "rudian", "1", "month", "2")
            if (mx != null) {
              Hbase_insert(conn, "rudian", "1", "month", "3", mx)
            }
            mx2 = Hbase_get(conn, "rudian", "1", "month", "1")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (ru_month - mx2.toInt).toDouble / mx2.toDouble
              Hbase_insert(conn, "rudian", "1", "h_month", "1", x.toString)
              Hbase_insert(conn, "rudian", "1", "month", "2", mx2)
            }
            Hbase_insert(conn, "rudian", "1", "month", "1", ru_month.toString)

            rur_month = ru_month.toDouble / ke_month.toDouble
            mx = Hbase_get(conn, "rudr", "1", "month", "2")
            if (mx != null) {
              Hbase_insert(conn, "rudr", "1", "month", "3", mx)
            }
            mx2 = Hbase_get(conn, "rudr", "1", "month", "1")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (rur_month - mx2.toDouble) / mx2.toDouble
              Hbase_insert(conn, "rudr", "1", "h_month", "1", x.toString)
              Hbase_insert(conn, "rudr", "1", "month", "2", mx2)
            }
            Hbase_insert(conn, "rudr", "1", "month", "1", rur_month.toString)

            xxx("zhouqi1", "month", zq1_month.toString)
            xxx("zhouqi2", "month", zq2_month.toString)
            xxx("zhouqi3", "month", zq3_month.toString)
            xxx("zhouqi4", "month", zq4_month.toString)
            xxx("zhouqi5", "month", zq5_month.toString)
            mx2 = Hbase_get(conn, "zhouqi1", "1", "h_month", "2")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (zq1_month - mx2.toDouble) / mx2.toDouble
              Hbase_insert(conn, "zhouqi1", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi1", "1", "h_month", "2", zq1_month.toString)
            mx2 = Hbase_get(conn, "zhouqi2", "1", "h_month", "2")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (zq2_month - mx2.toDouble) / mx2.toDouble
              Hbase_insert(conn, "zhouqi2", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi2", "1", "h_month", "2", zq2_month.toString)
            mx2 = Hbase_get(conn, "zhouqi3", "1", "h_month", "2")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (zq3_month - mx2.toDouble) / mx2.toDouble
              Hbase_insert(conn, "zhouqi3", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi3", "1", "h_month", "2", zq3_month.toString)
            mx2 = Hbase_get(conn, "zhouqi4", "1", "h_month", "2")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (zq4_month - mx2.toDouble) / mx2.toDouble
              Hbase_insert(conn, "zhouqi4", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi4", "1", "h_month", "2", zq4_month.toString)
            mx2 = Hbase_get(conn, "zhouqi5", "1", "h_month", "2")
            if (mx2 != null && mx2 != 0) {
              val x: Double = (zq5_month - mx2.toDouble) / mx2.toDouble
              Hbase_insert(conn, "zhouqi5", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhouqi5", "1", "h_month", "2", zq5_month.toString)

            xms = new_month.toDouble / old_month.toDouble
            hx2 = Hbase_get(conn, "new_old", "1", "h_month", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (xms - hx2.toDouble) / hx2.toDouble
              Hbase_insert(conn, "new_old", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "new_old", "1", "h_month", "2", xms.toString)
            xxx("new_old", "month", xms.toString)

            xxx("zhudian1", "month", zd1_month.toString)
            xxx("zhudian2", "month", zd2_month.toString)
            xxx("zhudian3", "month", zd3_month.toString)
            xxx("zhudian3", "month", zd4_month.toString)
            hx2 = Hbase_get(conn, "zhudian1", "1", "h_month", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd1_month - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian1", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian1", "1", "h_month", "2", zd1_month.toString)
            hx2 = Hbase_get(conn, "zhudian2", "1", "h_month", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd2_month - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian2", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian2", "1", "h_month", "2", zd2_month.toString)
            hx2 = Hbase_get(conn, "zhudian3", "1", "h_month", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd3_month - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian3", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian3", "1", "h_month", "2", zd3_month.toString)
            hx2 = Hbase_get(conn, "zhudian4", "1", "h_month", "2")
            if (hx2 != null && hx2 != 0) {
              val x: Double = (zd4_month - hx2.toInt).toDouble / hx2.toDouble
              Hbase_insert(conn, "zhudian4", "1", "h_month", "1", x.toString)
            }
            Hbase_insert(conn, "zhudian4", "1", "h_month", "2", zd4_month.toString)
            zd1_month = 0
            zd2_month = 0
            zd3_month = 0
            zd4_month = 0
            ru_month = 0
            ke_month = 0
            zq1_month = 0
            zq2_month = 0
            zq3_month = 0
            zq4_month = 0
            zq5_month = 0
            new_month = 0
            old_month = 0
          }
          else {
            ke_month += ke_hour
            ru_month += ru_hour
            zq1_month += zq1_hour
            zq2_month += zq2_hour
            zq3_month += zq3_hour
            zq4_month += zq4_hour
            zq5_month += zq5_hour
            new_month += new_hour
            old_month += old_hour
            zd1_month += zd1_hour
            zd2_month += zd2_hour
            zd3_month += zd3_hour
            zd4_month += zd4_hour
          }

          ke_hour = 0
          ru_hour = 0
          zq1_hour = 0
          zq2_hour = 0
          zq3_hour = 0
          zq4_hour = 0
          zq5_hour = 0
          new_hour = 0
          old_hour = 0
          zd1_hour = 0
          zd2_hour = 0
          zd3_hour = 0
          zd4_hour = 0
        }
        else {
          ke_hour += count_all
          ru_hour += count_in
          zq1_hour += t_num(0)
          zq2_hour += t_num(1)
          zq3_hour += t_num(2)
          zq4_hour += t_num(3)
          zq5_hour += t_num(4)
          new_hour += t_num(0)
          old_hour += count_in - x_num(0)
          zd1_hour += h_num(0)
          zd2_hour += h_num(1)
          zd3_hour += h_num(2)
          zd4_hour += h_num(3)
        }
        var time_dao = true
        while (time_dao) {
          val time2 = df.format(new Date())
          if (Times.Time_Duration_Seconds(time2, time1) >= 3)
            time_dao = false
        }
      }
      //val df=new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.ENGLISH)

    }
  }
}
