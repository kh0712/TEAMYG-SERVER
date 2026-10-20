-- 이미지 조회를 S3 원본 주소에서 aws.s3.public-base-url(운영은 CloudFront)로 옮긴다.
-- image_base_url은 spring.flyway.placeholders에서 주입되며 끝에 '/'가 없다.
-- 원본 주소가 아닌 행은 건드리지 않는다.

-- 1. URL이 바뀌는 캔버스의 version을 올린다. 그대로 두면 ETag가 같아 앱이 304를 받고
--    옛 S3 주소를 계속 쓰다가, 버킷 public 읽기를 막는 시점에 이미지가 깨진다.
--    URL을 바꾸기 전에 해야 대상 캔버스를 찾을 수 있다.
UPDATE parfait
SET version = version + 1
WHERE (background_type = 'IMAGE' AND background_value REGEXP '^https://[^/]+\\.amazonaws\\.com/')
   OR id IN (SELECT parfait_id
             FROM (SELECT DISTINCT parfait_id
                   FROM parfait_image
                   WHERE image_url REGEXP '^https://[^/]+\\.amazonaws\\.com/') AS changed);

-- 2. 이미지 원본
UPDATE image_meta
SET url = REGEXP_REPLACE(url, '^https://[^/]+\\.amazonaws\\.com/', '${image_base_url}/')
WHERE url REGEXP '^https://[^/]+\\.amazonaws\\.com/';

-- 3. 캔버스에 붙인 이미지는 배치 시점의 image_meta.url을 복사해 저장한다.
UPDATE parfait_image
SET image_url = REGEXP_REPLACE(image_url, '^https://[^/]+\\.amazonaws\\.com/', '${image_base_url}/')
WHERE image_url REGEXP '^https://[^/]+\\.amazonaws\\.com/';

-- 4. 배경 이미지도 선택 시점의 image_meta.url을 복사해 저장한다.
UPDATE parfait
SET background_value = REGEXP_REPLACE(background_value, '^https://[^/]+\\.amazonaws\\.com/', '${image_base_url}/')
WHERE background_type = 'IMAGE'
  AND background_value REGEXP '^https://[^/]+\\.amazonaws\\.com/';
