#ifndef __UART_H__
#define __UART_H__
#include	"config.h"

extern u16 USART1_RX_STA;
extern u8 USART1_RX_REC_ATCOMMAD;
extern u8 Flag_Rec_Message;
void Uart1Init(void);

void UART1_SendData(u8 dat);
void UART1_SendString(char *s);
//void UART1_SendString_other(char *s);

#define USART1_MAX_RECV_LEN		100				//

extern xdata u8  USART1_RX_BUF[USART1_MAX_RECV_LEN]; 	
extern u16 USART1_RX_STA; 
#endif