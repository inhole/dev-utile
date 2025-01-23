#!/bin/bash

APP_NAME="${projectName}-0.0.1-SNAPSHOT.jar"  # JAR 파일 이름
LOG_FILE="app.log"                           # 로그 파일 이름
PID_FILE="app.pid"                           # PID 파일 이름

case "$1" in
    start)
        if [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE") 2>/dev/null; then
            echo "Application is already running."
        else
            echo "Starting application..."
            nohup java -jar $APP_NAME > $LOG_FILE 2>&1 &
            echo $! > $PID_FILE
            echo "Application started with PID $(cat $PID_FILE). Logs: $LOG_FILE"
        fi
        ;;
    stop)
        if [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE") 2>/dev/null; then
            echo "Stopping application..."
            kill -9 $(cat "$PID_FILE") && rm -f "$PID_FILE"
            echo "Application stopped."
        else
            echo "Application is not running."
        fi
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    status)
        if [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE") 2>/dev/null; then
            echo "Application is running with PID $(cat $PID_FILE)."
        else
            echo "Application is not running."
        fi
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status}"
        exit 1
        ;;
esac
