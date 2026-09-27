import pymysql
from faker import Faker
from datetime import datetime, timedelta
import random
import hashlib
import base64

# MariaDB 연결 정보 설정
db_config = {
    "host": "localhost",
    "port": 3306,
    "user": "root",
    "password": "qwer1234",
    "database": "board",
}

# Faker 초기화
faker = Faker('ko_KR')

# MariaDB 연결
connection = pymysql.connect(**db_config)
cursor = connection.cursor()

# 테이블 이름 설정
member_table_name = "TB_MEMBER"
post_table_name = "TB_POST"
post_image_table_name = "TB_POST_IMAGE"
comment_table_name = "TB_COMMENT"
like_table_name = "TB_LIKE"
unlike_table_name = "TB_UNLIKE"

# 배치 사이즈 설정
batch_size = 100

# 비밀번호 해싱 함수
def hash_password(password):
    hash_object = hashlib.sha256(password.encode())
    return base64.b64encode(hash_object.digest()).decode()

# 회원 데이터 생성 및 삽입 함수
def member_data(num_rows):
    # 외래 키 검사를 비활성화
    cursor.execute("SET FOREIGN_KEY_CHECKS = 0;")

    # 인덱스 비활성화
    cursor.execute(f"ALTER TABLE {member_table_name} DISABLE KEYS;")

    try:
        # 기존 회원 수 확인 (이미 삽입된 회원 수 파악)
        cursor.execute(f"SELECT COUNT(*) FROM {member_table_name}")
        existing_count = cursor.fetchone()[0]
        print(f"현재 회원 수: {existing_count}")
        
        insert_values = []

        for i in range(1, num_rows + 1):
            id = faker.user_name() + str(random.randint(1000, 9999))
            password = hash_password("1234")
            user_name = faker.name()
            nick_name = f"{user_name}_{random.randint(1, 100)}"
            is_email_auth = random.choice([True, False])
            is_in_active = random.choice([True, False])
            # 이메일에 고유 식별자 추가하여 중복 방지
            email = f"{faker.user_name()}_{i}_{random.randint(1000, 9999)}@{faker.domain_name()}"
            phone_number = faker.phone_number()
            profile_image_url = f"/upload/{faker.uuid4()}.png" if random.random() > 0.3 else None
            created_at = faker.date_time_this_year()
            updated_at = created_at + timedelta(days=random.randint(0, 10))

            insert_values.append((
                id,
                password,
                user_name,
                nick_name,
                is_email_auth,
                is_in_active,
                email,
                phone_number,
                profile_image_url,
                created_at.strftime('%Y-%m-%d %H:%M:%S'),
                updated_at.strftime('%Y-%m-%d %H:%M:%S')
            ))

            # 배치 사이즈에 도달하면 삽입
            if len(insert_values) >= batch_size:
                try:
                    cursor.executemany(f"""
                        INSERT INTO {member_table_name} (
                            id, password, user_name, nick_name, is_email_auth, is_in_active,
                            email, phone_number, profile_image_url, created_at, updated_at
                        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
                    """, insert_values)
                    connection.commit()
                    print(f"------------------------- member_data {len(insert_values)} inserted successfully.")
                except Exception as e:
                    print(f"배치 삽입 중 오류 발생: {e}")
                    connection.rollback()
                finally:
                    insert_values = []

        # 남은 데이터 삽입
        if insert_values:
            try:
                cursor.executemany(f"""
                    INSERT INTO {member_table_name} (
                        id, password, user_name, nick_name, is_email_auth, is_in_active,
                        email, phone_number, profile_image_url, created_at, updated_at
                    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
                """, insert_values)
                connection.commit()
                print(f"------------------------- member_data 나머지 {len(insert_values)}개 inserted successfully.")
            except Exception as e:
                print(f"나머지 데이터 삽입 중 오류 발생: {e}")
                connection.rollback()

        # 최종 회원 수 확인
        cursor.execute(f"SELECT COUNT(*) FROM {member_table_name}")
        final_count = cursor.fetchone()[0]
        print(f"최종 회원 수: {final_count}, 추가된 회원 수: {final_count - existing_count}")

    except Exception as e:
        print(f"예상치 못한 오류 발생: {e}")
        connection.rollback()

    finally:
        # 인덱스 다시 활성화
        cursor.execute(f"ALTER TABLE {member_table_name} ENABLE KEYS;")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1;")

# 게시물 데이터 생성 및 삽입 함수
def post_data(num_rows):
    # 외래 키 검사를 비활성화
    cursor.execute("SET FOREIGN_KEY_CHECKS = 0;")

    # 인덱스 비활성화
    cursor.execute(f"ALTER TABLE {post_table_name} DISABLE KEYS;")

    try:
        # 회원 ID 목록 조회
        cursor.execute(f"SELECT idx FROM {member_table_name}")
        member_ids = [row[0] for row in cursor.fetchall()]
        
        if not member_ids:
            print("회원이 없습니다. 먼저 회원 데이터를 생성해주세요.")
            return
        
        # 카테고리 ID 목록 조회 (CATEGORY 그룹)
        cursor.execute("SELECT idx FROM TB_CODE WHERE group_name = 'CATEGORY'")
        category_ids = [row[0] for row in cursor.fetchall()]
        
        if not category_ids:
            print("카테고리가 없습니다. 먼저 코드 데이터를 생성해주세요.")
            return
        
        # 공개 범위 ID 목록 조회 (VISIBILITY 그룹)
        cursor.execute("SELECT idx FROM TB_CODE WHERE group_name = 'VISIBILITY'")
        range_ids = [row[0] for row in cursor.fetchall()]
        
        if not range_ids:
            print("공개 범위가 없습니다. 먼저 코드 데이터를 생성해주세요.")
            return
        
        insert_values = []

        for i in range(1, num_rows + 1):
            member_idx = random.choice(member_ids)
            category_idx = random.choice(category_ids)
            range_idx = random.choice(range_ids)
            title = faker.sentence(nb_words=random.randint(5, 10))
            content = faker.text(max_nb_chars=random.randint(200, 1000))
            view_count = random.randint(0, 1000)
            created_at = faker.date_time_this_year()
            updated_at = created_at + timedelta(days=random.randint(0, 10))

            insert_values.append((
                member_idx,
                category_idx,
                range_idx,
                title,
                content,
                view_count,
                created_at.strftime('%Y-%m-%d %H:%M:%S'),
                updated_at.strftime('%Y-%m-%d %H:%M:%S')
            ))

            # 배치 사이즈에 도달하면 삽입
            if len(insert_values) >= batch_size:
                cursor.executemany(f"""
                    INSERT INTO {post_table_name} (
                        member_idx, category_idx, range_idx, title, content, 
                        view_count, created_at, updated_at
                    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s);
                """, insert_values)
                insert_values = []
                print(f"------------------------- post_data {batch_size} inserted successfully.")

        # 남은 데이터 삽입
        if insert_values:
            cursor.executemany(f"""
                INSERT INTO {post_table_name} (
                    member_idx, category_idx, range_idx, title, content, 
                    view_count, created_at, updated_at
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s);
            """, insert_values)

        # 커밋
        connection.commit()
        print(f"------------------------- post_data total inserted successfully.")

    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        connection.rollback()

    finally:
        # 인덱스 다시 활성화
        cursor.execute(f"ALTER TABLE {post_table_name} ENABLE KEYS;")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1;")

# 게시물 이미지 데이터 생성 및 삽입 함수
def post_image_data(num_rows):
    # 외래 키 검사를 비활성화
    cursor.execute("SET FOREIGN_KEY_CHECKS = 0;")

    # 인덱스 비활성화
    cursor.execute(f"ALTER TABLE {post_image_table_name} DISABLE KEYS;")

    try:
        insert_values = []

        # 게시물 ID 조회
        cursor.execute(f"SELECT idx FROM {post_table_name}")
        post_ids = [row[0] for row in cursor.fetchall()]
        
        if not post_ids:
            print("게시물이 없습니다. 먼저 게시물 데이터를 생성해주세요.")
            return
        
        for i in range(1, num_rows + 1):
            post_idx = random.choice(post_ids)
            image_url = f"https://picsum.photos/id/{random.randint(1, 1000)}/{random.randint(300, 800)}/{random.randint(300, 800)}"
            created_at = faker.date_time_this_year()

            insert_values.append((
                post_idx,
                image_url,
                created_at.strftime('%Y-%m-%d %H:%M:%S')
            ))

            # 배치 사이즈에 도달하면 삽입
            if len(insert_values) >= batch_size:
                cursor.executemany(f"""
                    INSERT INTO {post_image_table_name} (
                        post_idx, image_url, created_at
                    ) VALUES (%s, %s, %s);
                """, insert_values)
                insert_values = []
                print(f"------------------------- post_image_data {batch_size} inserted successfully.")

        # 남은 데이터 삽입
        if insert_values:
            cursor.executemany(f"""
                INSERT INTO {post_image_table_name} (
                    post_idx, image_url, created_at
                ) VALUES (%s, %s, %s);
            """, insert_values)

        # 커밋
        connection.commit()
        print(f"------------------------- post_image_data total inserted successfully.")

    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        connection.rollback()

    finally:
        # 인덱스 다시 활성화
        cursor.execute(f"ALTER TABLE {post_image_table_name} ENABLE KEYS;")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1;")

# 댓글 데이터 생성 및 삽입 함수
def comment_data(num_rows, parent_ratio=0.2):
    # 외래 키 검사를 비활성화
    cursor.execute("SET FOREIGN_KEY_CHECKS = 0;")

    # 인덱스 비활성화
    cursor.execute(f"ALTER TABLE {comment_table_name} DISABLE KEYS;")

    try:
        insert_values = []

        # 게시물 ID 조회
        cursor.execute(f"SELECT idx FROM {post_table_name}")
        post_ids = [row[0] for row in cursor.fetchall()]
        
        if not post_ids:
            print("게시물이 없습니다. 먼저 게시물 데이터를 생성해주세요.")
            return
        
        # 회원 ID 조회
        cursor.execute(f"SELECT idx FROM {member_table_name}")
        member_ids = [row[0] for row in cursor.fetchall()]
        
        if not member_ids:
            print("회원이 없습니다. 먼저 회원 데이터를 생성해주세요.")
            return
        
        parent_comments = []  # 부모 댓글 ID를 저장할 리스트
        
        for i in range(1, num_rows + 1):
            post_idx = random.choice(post_ids)
            member_idx = random.choice(member_ids)
            
            # 일부 댓글은 대댓글로 만듦 (parent_comment_idx가 있음)
            parent_comment_idx = None
            if parent_comments and random.random() < parent_ratio:
                parent_comment_idx = random.choice(parent_comments)
            
            content = faker.text(max_nb_chars=random.randint(50, 300))
            created_at = faker.date_time_this_year()
            updated_at = created_at + timedelta(days=random.randint(0, 10))

            insert_values.append((
                post_idx,
                member_idx,
                parent_comment_idx,
                content,
                created_at.strftime('%Y-%m-%d %H:%M:%S'),
                updated_at.strftime('%Y-%m-%d %H:%M:%S')
            ))

            # 배치 사이즈에 도달하면 삽입
            if len(insert_values) >= batch_size:
                cursor.executemany(f"""
                    INSERT INTO {comment_table_name} (
                        post_idx, member_idx, parent_comment_idx, content, created_at, updated_at
                    ) VALUES (%s, %s, %s, %s, %s, %s);
                """, insert_values)
                
                # 방금 삽입한 댓글들의 ID를 가져와서 부모 댓글 목록에 추가
                if parent_ratio > 0:
                    cursor.execute(f"SELECT idx FROM {comment_table_name} ORDER BY idx DESC LIMIT {batch_size}")
                    new_comments = [row[0] for row in cursor.fetchall()]
                    parent_comments.extend(new_comments)
                
                insert_values = []
                print(f"------------------------- comment_data {batch_size} inserted successfully.")

        # 남은 데이터 삽입
        if insert_values:
            cursor.executemany(f"""
                INSERT INTO {comment_table_name} (
                    post_idx, member_idx, parent_comment_idx, content, created_at, updated_at
                ) VALUES (%s, %s, %s, %s, %s, %s);
            """, insert_values)

        # 커밋
        connection.commit()
        print(f"------------------------- comment_data total inserted successfully.")

    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        connection.rollback()

    finally:
        # 인덱스 다시 활성화
        cursor.execute(f"ALTER TABLE {comment_table_name} ENABLE KEYS;")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1;")

# 좋아요 데이터 생성 및 삽입 함수
def like_data(num_rows, post_ratio=0.7):
    # 외래 키 검사를 비활성화
    cursor.execute("SET FOREIGN_KEY_CHECKS = 0;")

    # 인덱스 비활성화
    cursor.execute(f"ALTER TABLE {like_table_name} DISABLE KEYS;")

    try:
        insert_values = []

        # 게시물 ID 조회
        cursor.execute(f"SELECT idx FROM {post_table_name}")
        post_ids = [row[0] for row in cursor.fetchall()]
        
        # 댓글 ID 조회
        cursor.execute(f"SELECT idx FROM {comment_table_name}")
        comment_ids = [row[0] for row in cursor.fetchall()]
        
        # 회원 ID 조회
        cursor.execute(f"SELECT idx FROM {member_table_name}")
        member_ids = [row[0] for row in cursor.fetchall()]
        
        if not member_ids or (not post_ids and not comment_ids):
            print("회원, 게시물 또는 댓글 데이터가 없습니다. 먼저 필요한 데이터를 생성해주세요.")
            return
        
        # 이미 처리한 조합을 추적하기 위한 세트
        processed_combinations = set()
        
        for i in range(1, num_rows + 1):
            member_idx = random.choice(member_ids)
            
            # post_ratio 확률로 게시물에 좋아요, 나머지는 댓글에 좋아요
            if random.random() < post_ratio and post_ids:
                post_idx = random.choice(post_ids)
                comment_idx = None
                combo = (post_idx, None, member_idx)
            elif comment_ids:
                post_idx = None
                comment_idx = random.choice(comment_ids)
                combo = (None, comment_idx, member_idx)
            else:
                continue
            
            # 이미 처리한 조합이면 건너뜀
            if combo in processed_combinations:
                continue
            
            processed_combinations.add(combo)
            insert_values.append((post_idx, comment_idx, member_idx))

            # 배치 사이즈에 도달하면 삽입
            if len(insert_values) >= batch_size:
                cursor.executemany(f"""
                    INSERT INTO {like_table_name} (post_idx, comment_idx, member_idx)
                    VALUES (%s, %s, %s);
                """, insert_values)
                insert_values = []
                print(f"------------------------- like_data {batch_size} inserted successfully.")

        # 남은 데이터 삽입
        if insert_values:
            cursor.executemany(f"""
                INSERT INTO {like_table_name} (post_idx, comment_idx, member_idx)
                VALUES (%s, %s, %s);
            """, insert_values)

        # 커밋
        connection.commit()
        print(f"------------------------- like_data total inserted successfully.")

    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        connection.rollback()

    finally:
        # 인덱스 다시 활성화
        cursor.execute(f"ALTER TABLE {like_table_name} ENABLE KEYS;")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1;")

# 싫어요 데이터 생성 및 삽입 함수
def unlike_data(num_rows, post_ratio=0.7):
    # 외래 키 검사를 비활성화
    cursor.execute("SET FOREIGN_KEY_CHECKS = 0;")

    # 인덱스 비활성화
    cursor.execute(f"ALTER TABLE {unlike_table_name} DISABLE KEYS;")

    try:
        insert_values = []

        # 게시물 ID 조회
        cursor.execute(f"SELECT idx FROM {post_table_name}")
        post_ids = [row[0] for row in cursor.fetchall()]
        
        # 댓글 ID 조회
        cursor.execute(f"SELECT idx FROM {comment_table_name}")
        comment_ids = [row[0] for row in cursor.fetchall()]
        
        # 회원 ID 조회
        cursor.execute(f"SELECT idx FROM {member_table_name}")
        member_ids = [row[0] for row in cursor.fetchall()]
        
        if not member_ids or (not post_ids and not comment_ids):
            print("회원, 게시물 또는 댓글 데이터가 없습니다. 먼저 필요한 데이터를 생성해주세요.")
            return
        
        # 이미 처리한 조합을 추적하기 위한 세트
        processed_combinations = set()
        
        for i in range(1, num_rows + 1):
            member_idx = random.choice(member_ids)
            
            # post_ratio 확률로 게시물에 싫어요, 나머지는 댓글에 싫어요
            if random.random() < post_ratio and post_ids:
                post_idx = random.choice(post_ids)
                comment_idx = None
                combo = (post_idx, None, member_idx)
            elif comment_ids:
                post_idx = None
                comment_idx = random.choice(comment_ids)
                combo = (None, comment_idx, member_idx)
            else:
                continue
            
            # 이미 처리한 조합이면 건너뜀
            if combo in processed_combinations:
                continue
            
            processed_combinations.add(combo)
            insert_values.append((post_idx, comment_idx, member_idx))

            # 배치 사이즈에 도달하면 삽입
            if len(insert_values) >= batch_size:
                cursor.executemany(f"""
                    INSERT INTO {unlike_table_name} (post_idx, comment_idx, member_idx)
                    VALUES (%s, %s, %s);
                """, insert_values)
                insert_values = []
                print(f"------------------------- unlike_data {batch_size} inserted successfully.")

        # 남은 데이터 삽입
        if insert_values:
            cursor.executemany(f"""
                INSERT INTO {unlike_table_name} (post_idx, comment_idx, member_idx)
                VALUES (%s, %s, %s);
            """, insert_values)

        # 커밋
        connection.commit()
        print(f"------------------------- unlike_data total inserted successfully.")

    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        connection.rollback()

    finally:
        # 인덱스 다시 활성화
        cursor.execute(f"ALTER TABLE {unlike_table_name} ENABLE KEYS;")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1;")

try:
    # 테스트 데이터 생성 실행 (데이터 수는 조정 가능)
    member_data(1000)          # 1000명의 회원 생성
    post_data(5000)            # 5000개의 게시물 생성
    post_image_data(10000)     # 10000개의 게시물 이미지 생성 (게시물당 평균 2개)
    comment_data(200000, 0.2)   # 20000개의 댓글 생성 (20%는 대댓글)
    like_data(15000, 0.7)      # 15000개의 좋아요 생성 (70%는 게시물에, 30%는 댓글에)
    unlike_data(15000, 0.7)     # 5000개의 싫어요 생성 (70%는 게시물에, 30%는 댓글에)

except pymysql.MySQLError as e:
    print(f"MySQL Error: {e}")
except Exception as e:
    print(f"Error: {e}")
finally:
    cursor.close()
    connection.close()
    print("데이터베이스 연결이 닫혔습니다.")
