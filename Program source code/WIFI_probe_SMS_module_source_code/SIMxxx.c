#include "SIMxxx.h"

u8 SIM900_CSQ[4];
u8* sim900a_check_cmd(u8 *str)
{
	char *strx=0;
	if(USART1_RX_STA&0X8000)		//
	{ 
		USART1_RX_BUF[USART1_RX_STA&0X7FFF]=0;//
		strx=strstr((const char*)USART1_RX_BUF,(const char*)str);
	} 
	return (u8*)strx;
}
u8 sim900a_send_cmd(u8 *cmd,u8 *ack,u16 waittime)
{	
	u8 res=0;
	USART1_RX_STA=0;USART1_RX_REC_ATCOMMAD=1;
	UART1_SendString(cmd);
	if(ack&&waittime)		
	{
		while(--waittime)	
		{
			delay_ms(10);
			if(USART1_RX_STA&0X8000)
			{
				if(sim900a_check_cmd(ack))break; 
				USART1_RX_STA=0;
			} 
		}
		if(waittime==0)res=1; 
	}
	USART1_RX_STA=0;USART1_RX_REC_ATCOMMAD=0;
	return res;
}
u8 sim900a_work_test(void)
{
	if(sim900a_send_cmd((u8 *)"AT\r\n",(u8 *)"OK",100))
	{
		if(sim900a_send_cmd((u8 *)"AT\r\n",(u8 *)"OK",100))return SIM_COMMUNTION_ERR;	//通信不上
	}		
	if(sim900a_send_cmd((u8 *)"AT+CPIN?\r\n",(u8 *)"READY",400))return SIM_CPIN_ERR;	//没有SIM卡
	if(sim900a_send_cmd((u8 *)"AT+CREG?\r\n",(u8 *)"0,1",400))
	{
		if(strstr((const char*)USART1_RX_BUF,"0,5")==NULL)
		{
			 if(!sim900a_send_cmd((u8 *)"AT+CSQ\r\n",(u8 *)"OK",200))	
			 {
				memcpy(SIM900_CSQ,USART1_RX_BUF+15,2);
			 }
			 return SIM_CREG_FAIL;	//等待附着到网络
		}
	}
	return SIM_OK;
}
u8 sim900a_enmessage_mode_out(void)	//让GSM板收到的英文或数字短信不转码
{	
	if(sim900a_send_cmd((u8 *)"AT+CMGF=1\r\n",(u8 *)"OK",100))	return SIM_CMGF_ERR;
	if(sim900a_send_cmd((u8 *)"AT+CSCS=\"GSM\"\r\n",(u8 *)"OK",100))	return SIM_CSCS_ERR;
	if(sim900a_send_cmd((u8 *)"AT+CSCA?\r\n",(u8 *)"OK",100))	return SIM_CSCA_ERR;
	if(sim900a_send_cmd((u8 *)"AT+CSMP=17,167,0,241\r\n",(u8 *)"OK",100))	return SIM_CSMP_ERR;
	if(sim900a_send_cmd((u8 *)"AT+CNMI=2,2\r\n",(u8 *)"OK",200))return SIM_CNMI_ERR;	//短信直接输出
	return SIM_OK;
}
u8 SIM_HANDLE_MESSAGE_CMD(void)
{
	if(strstr(USART1_RX_BUF,"on")!=NULL)
	{
		return 1;
	}
	else if(strstr(USART1_RX_BUF,"off")!=NULL)	return 2;
	return 0;
}

