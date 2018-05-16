//ÑÓÊ±º¯Êý
#include	"delay.h"
 #include "intrins.h" 
void Delay1ms()		//@11.0592MHz
{
	unsigned char i, j;

	_nop_();
	i = 2;
	j = 199;
	do
	{
		while (--j);
	} while (--i);
}

void delay_ms(u16 ms)
{
	u16 i;
	for(i=0;i<ms;i++)
		Delay1ms();
}
