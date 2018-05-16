#ifndef __timer0_H__
#define __timer0_H__

#include	"config.h"
#include	"uart.h"
#include "string.h"
extern void Timer0Init(void);
extern void TIM_SetCounter(void);			  //重新装值
#endif
