/**
  * Created by hadoop on 17-5-26.
  */

import java.text.SimpleDateFormat
import java.util.Locale

import com.github.nscala_time.time.Imports._
object Times {
  val sdf= new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.ENGLISH)
  //把时间转为数字形式
  def Time_cnum(string: String): String ={
    val data=new DateTime(sdf.parse(string))
    data.toString()
  }
  def Time_Duration_Minutes(string: String,string1: String): Long ={
    val datetime=new DateTime(sdf.parse(string))
    val data2=new DateTime(sdf.parse(string1))
    val d=new Duration(data2,datetime)
    d.getStandardMinutes
  }
  def Time_Duration_Hours(string: String,string1: String): Long ={
    val datetime=new DateTime(sdf.parse(string))
    val data2=new DateTime(sdf.parse(string1))
    val d=new Duration(data2,datetime)
    d.getStandardHours
  }
  def Time_Duration_Seconds(string: String,string1: String): Long ={
    val datetime=new DateTime(sdf.parse(string))
    val data2=new DateTime(sdf.parse(string1))
    val d=new Duration(data2,datetime)
    d.getStandardSeconds
  }
  def Time_Duration_Days(string: String,string1: String): Long ={
    val datetime=new DateTime(sdf.parse(string))
    val data2=new DateTime(sdf.parse(string1))
    val d=new Duration(data2,datetime)
    d.getStandardDays
  }
}
