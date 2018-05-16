/**********************************************************************************
 //SIMXXX系列开发板底层代码
 //全球鹰电子@UNV
 //版本号;V1.0
 //官方淘宝店地址：https://shop110330041.taobao.com
 //版权所有，盗版必究
* 工程名  :短信控制LED灯
 * 描述    :通过向GSM板发送控制命令短信，来控制单片机板上的LED灯，控制命令为：on 或是off。 
 * 实验平台:STC89XX
 * 库版本  :

**********************************************************************************/

#include "string.h"
#include "delay.h"
#include "uart.h"
#include "timer0.h"
#include "SIMxxx.h"

sbit P10=P0^0;	//用于提示程序进行到哪里
sbit P11=P0^1;	//用于提示程序进行到哪里
sbit RUNING_LED=P0^2;	//程序中控制的LED灯
/*************  外部函数和变量声明*****************/
/*******************************************************************************
* 函数名 : main 
* 描述   : 主函数
* 输入   : 
* 输出   : 
* 返回   : 
* 注意   : 
*******************************************************************************/
void main(void)
{
	u8 res;
	Uart1Init();
	Timer0Init();
	EA=1;	//开总中断
	res=1;
	P10=0;
	RUNING_LED=0;

	while(res)
	{
		res=sim900a_work_test();
	}
	res=1;
	while(res)
	{
		res=sim900a_enmessage_mode_out();
	} 
	P10=1;
	res=1;
	while(1)
	{
		if(Flag_Rec_Message==1)	
		{
			Flag_Rec_Message=0;
			res=SIM_HANDLE_MESSAGE_CMD();
		   if(res)
			{
				if(res==1)		RUNING_LED=0;
				else if(res==2)		RUNING_LED=1;
			} 
		}		
	}	
}




