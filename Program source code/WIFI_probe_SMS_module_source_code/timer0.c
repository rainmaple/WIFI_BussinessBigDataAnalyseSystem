#include "TIMER0.h"
sbit RUNING_LED=P2^1;

void Timer0Init(void)		//10毫秒@115200
{
	AUXR &= 0x7F;		
	TMOD &= 0xF0;		
	TMOD |= 0x01;		//
	TL0 = 0x00;		//
	TH0 = 0xDC;		//
	TF0 = 0;		//
	ET0 = 1;    	  //
	TR0 = 0;		//停止计时
}
void TIM_SetCounter(void)			  //重新装值
{
	TL0 = 0x00;		//
	TH0 = 0xDC;		//
}
/*******************************************************************************
* 函数名 : Timer0_ISR
* 描述   : 定时器0中断服务入口函数,20ms中断一次
* 输入   : 
* 输出   : 
* 返回   : 
* 注意   : 
*******************************************************************************/
void Timer0_ISR() interrupt 1
{
	TR0=0;//关定时器
	USART1_RX_STA|=1<<15;	//
	USART1_RX_BUF[USART1_RX_STA&0X7FFF]=0;//添加结束符
	if(!USART1_RX_REC_ATCOMMAD)	
	{		
		USART1_RX_STA=0;		
		if(strstr((char*)USART1_RX_BUF,"CMT:")!=NULL)	Flag_Rec_Message=1;	//检测是否来短信了
	}	
}