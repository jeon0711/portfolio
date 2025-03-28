#!/bin/bash

# 설정
IMAGE_NAME="jeon-app"
JAR_PATH="build/libs/portfolio.jar"
OUTPUT_TAR="jeon-app.tar"
DOCKERFILE="Dockerfile"
./gradlew clean build
# 🔍 유효성 검사
if [ ! -f "$JAR_PATH" ]; then
  echo "❌ JAR 파일이 존재하지 않습니다: $JAR_PATH"
  exit 1
fi

if [ ! -f "$DOCKERFILE" ]; then
  echo "❌ Dockerfile이 존재하지 않습니다: $DOCKERFILE"
  exit 1
fi

echo "🐳 Docker 이미지 빌드 중..."
docker build -t $IMAGE_NAME .

if [ $? -ne 0 ]; then
  echo "❌ 이미지 빌드 실패"
  exit 1
fi

echo "💾 Docker 이미지 저장 중 → $OUTPUT_TAR"
docker save -o $OUTPUT_TAR $IMAGE_NAME
chmod 644 $OUTPUT_TAR

echo "✅ 빌드 및 저장 완료: $IMAGE_NAME → $OUTPUT_TAR"

