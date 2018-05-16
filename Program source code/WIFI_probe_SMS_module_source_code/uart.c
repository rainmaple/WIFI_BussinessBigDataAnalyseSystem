#include "uart.h"
#include "timer0.h"

u16 USART1_RX_STA=0;  
u8 USART1_RX_REC_ATCOMMAD;
u8 Flag_Rec_Message=0;
xdata u8 USART1_RX_BUF[USART1_MAX_RECV_LEN]; 				//

void Uart1Init(void)		//9600bps@11.05926MHz
{
	PCON &= 0x7F;		//
	SCON = 0x50;		//
	AUXR &= 0xBF;		//
	AUXR &= 0xFE;		//
	TMOD &= 0x0F;		//
	TMOD |= 0x20;		//
	TL1 = 0xFD;		  //
	TH1 = 0xFD;		  //
	ET1 = 0;		    //
	TR1 = 1;		    //
  ES=1;					//
}
/*----------------------------
UART1 发送串口数据
-----------------------------*/
void UART1_SendData(u8 dat)
{
	ES=0;					//关串口中断
	SBUF=dat;			
	while(TI!=1);	//等待发送成功
	TI=0;					//清除发送中断标志
	ES=1;					//开串口中断
}
/*----------------------------
UART1 发送字符串
-----------------------------*/
void UART1_SendString(char *s)
{
	while(*s)//检测字符串结束符
	{
		UART1_SendData(*s++);//发送当前字符
	}
}
///*----------------------------
//UART1 发送字符串
//避免出现 软件报重复调用
//-----------------------------*/
//void UART1_SendString_other(char *s)
//{
//	while(*s)//检测字符串结束符
//	{
//		UART1_SendData(*s++);//发送当前字符
//	}
//}
/*******************************************************************************
* 函数名 : Uart1 
* 描述   : 串口1中断服务入口函数
* 输入   : 
* 输出   : 
* 返回   : 
* 注意   : 
******************************/
void Uart1_INTER() interrupt 4
{
	if (RI)
    {
      RI = 0;                 //清除RI位  
	  if(USART1_RX_STA<10000000)		//?1?éò??óê?êy?Y
	  {	  	
		TIM_SetCounter();
		if(USART1_RX_STA==0)	TR0=1;	 	//开启定时器
		USART1_RX_BUF[USART1_RX_STA++]=SBUF;		//保存串口数据
	  }
	  else 
	  {
		USART1_RX_STA|=1<<15;
  	  } 
    }
    if (TI)
    {
        TI = 0;                 //清除TI位
    }
}


