#ifndef __LOG_H__
#define __LOG_H__

#include <stdio.h>

#define LOG_VERBOSE 1
#define LOG_DEBUG 2
#define LOG_INFO 3
#define LOG_WARN 4
#define LOG_ERROR 5
#define LOG_FATAL 6
#define LOG_SILENT 7

#define LOG_LEVEL LOG_VERBOSE

#ifndef LOG_LEVEL
#define LOG_LEVEL LOG_SILENT
#endif

//默认的日志tag
#define TAG ""
#ifndef TAG
#define TAG (strrchr(__FILE__, '/') ? strrchr(__FILE__, '/') + 1 : __FILE__)
#endif

#if(LOG_LEVEL <= LOG_VERBOSE)
#define LOGV(tag, fmt, ...) do { printf(fmt, ##__VA_ARGS__); fflush(stdout); } while(0);
#else
#define LOGV(tag,...)
#endif

#if(LOG_LEVEL <= LOG_DEBUG)
#define LOGD(tag, fmt, ...) do { printf(fmt, ##__VA_ARGS__); fflush(stdout); } while(0);
#else
#define LOGD(tag,...)
#endif

#if(LOG_LEVEL <= LOG_INFO)
#define LOGI(tag, fmt, ...) do { printf(fmt, ##__VA_ARGS__); fflush(stdout); } while(0);
#else
#define LOGI(tag,...)
#endif

#if(LOG_LEVEL <= LOG_WARN)
#define LOGW(tag, fmt, ...) do { printf(fmt, ##__VA_ARGS__); fflush(stdout); } while(0);
#else
#define LOGW(tag,...)
#endif

#if(LOG_LEVEL  <= LOG_ERROR)
#define LOGE(tag, fmt, ...) do { printf(fmt, ##__VA_ARGS__); fflush(stdout); } while(0);
#else
#define LOGE(tag,...)
#endif

#if(LOG_LEVEL <= LOG_FATAL)
#define LOGF(tag, fmt, ...) do { printf(fmt, ##__VA_ARGS__); fflush(stdout); } while(0);
#else
#define LOGF(tag,...)
#endif

#endif /* LOG_H_ */

