#!/bin/bash
BUILD_PATH=$(ls /home/ubuntu/app/api-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_PATH)
echo "> build 파일명: $JAR_NAME"

echo "> build 파일 복사"
DEPLOY_PATH=/home/ubuntu/app/nonstop/jar/
cp $BUILD_PATH $DEPLOY_PATH

echo "> PROFILE = DEV로 설정"
PROFILE=DEV

echo "> application.jar 교체"
RUNNING_APPLICATION=$PROFILE-GunbbangServer.jar   # ??? 그냥 이름 임의로 지정해놓는듯
RUNNING_APPLICATION_PATH=$DEPLOY_PATH$RUNNING_APPLICATION

ln -Tfs $DEPLOY_PATH$JAR_NAME $RUNNING_APPLICATION_PATH

echo "> 현재 구동중인 애플리케이션 pid 확인"
RUNNING_PID=$(pgrep -f $RUNNING_APPLICATION)

if [ -z $RUNNING_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $RUNNING_PID"
  kill -15 $RUNNING_PID
  exit=$?
  sleep 60
  if [ exit -eq 0 ]; then
      echo "kill -15 명령어로 서버가 다운되었습니다. exit status $exit. kill -15로 종료된 시각: $(date) "
  else
      echo "kill -15 명령어로 서버가 다운되지 않았습니다. exit status $exit."
      echo "kill -9 명령어로 서버를 다운시킵니다. kill -9로 종료 시작한 시각: $(date)"
      kill -9 $RUNNING_PID
  fi
  sleep 30
fi

echo "> $PROFILE 배포"
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=$PROFILE $RUNNING_APPLICATION_PATH >> /home/ubuntu/app/nohup/nohup_$(date +\%Y_\%m_\%d_\%H_\%M_\%S).out 2>&1 &

current_datetime=$(date "+%Y년 %m월 %d일 %H시 %M분 %S초")
echo "배포된 시간: $current_datetime"
