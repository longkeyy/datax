/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


#include "strsplit.h"
CStrSplit::CStrSplit()
     :m_nFieldCnt(0),
     	m_pLine(NULL)
{

}

int CStrSplit::Init()
{
     m_ppFields = new char*[MAX_FIELD_CNT];
     if ( m_ppFields == NULL )
     {
		 return -1; 
     }
     for ( int n = 0; n < MAX_FIELD_CNT; n++ )
     {
          m_ppFields[n] = new char[MAX_FIELD_SIZE];
          if ( m_ppFields[n] == NULL )
          {
			  return -1;
          }
          memset( m_ppFields[n], 0, MAX_FIELD_SIZE );
     }

	 return 0;
}

CStrSplit::~CStrSplit()
{
     for ( int n = 0; n < MAX_FIELD_CNT; n++ )
     {
          delete m_ppFields[n];
     }
     delete[] m_ppFields;
}

int CStrSplit::Split(const char* pSourStr, const char* del)
{
     m_pLine = pSourStr;
     m_nFieldCnt = 0;
     
     int delLen = strlen(del);
     int fieldLen = 0;
     const char* pStart = pSourStr;
     
     const char* pNext = strstr(pStart, del); 
     while ( pNext != NULL )
     {
          fieldLen = pNext - pStart;
          strncpy( m_ppFields[m_nFieldCnt], pStart, fieldLen );
          m_ppFields[m_nFieldCnt][fieldLen] = '\0';
          pStart = pNext + delLen;
          pNext = strstr( pStart, del );
          m_nFieldCnt++;
     }
     if ( pStart != '\0' )
     {
          strcpy( m_ppFields[m_nFieldCnt], pStart );
          m_ppFields[m_nFieldCnt][strlen(pStart)] = '\0';
          m_nFieldCnt++;
     }
     return m_nFieldCnt;
}

int CStrSplit::GetFieldCount()
{
     return m_nFieldCnt;
}

const char* CStrSplit::GetField(int n)
{
     if ( n == 0 )
     {
          return m_pLine;
     }
     return n > 0 ? m_ppFields[n-1] : NULL;
}

const char* CStrSplit::operator[] (int n)
{
     if ( n == 0 )
     {
          return m_pLine;
     }
     return n > 0 ? m_ppFields[n-1] : NULL;
}
