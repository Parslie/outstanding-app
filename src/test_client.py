import tempfile
import os
import pytest

from server import app
from extensions import db

user_url = "http://127.0.0.1:5000/user"
post_url = "http://127.0.0.1:5000/post"

user_one = {"username": "viktor", "email": "viktor@viktor.viktor", "password": "viktor", "password_confirmation": "viktor"}
user_two = {"username": "isak", "email": "isak@isak.isak", "password": "isak", "password_confirmation": "isak"}


@pytest.fixture
def client():
    db_fd, app.config['DATABASE_FILE_PATH'] = tempfile.mkstemp()
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + app.config['DATABASE_FILE_PATH']
    app.config['TESTING'] = True

    client = app.test_client()

    with app.app_context():
        db.drop_all()
        db.create_all()

    yield client

    os.close(db_fd)
    os.unlink(app.config['DATABASE_FILE_PATH'])


def test_user(client):
    reg_resp = client.post(user_url, json=user_one)
    login_resp = client.post(user_url + "/login", json=user_one)
    assert reg_resp.status_code == 200 and login_resp.status_code == 200

    auth_token = login_resp.json.get("auth_token")
    auth_user_id = login_resp.json.get("auth_user_id")
    headers = {"Authorization": "Bearer " + auth_token}

    get_user_resp = client.get(user_url + "/" + auth_user_id, headers=headers)
    assert get_user_resp.json.get("username") == user_one.get("username")

    new_account = {"username": "username", "email": "email@email.email", "password": "12345"}
    set_acc_resp = client.post(user_url + "/account", headers=headers, json=new_account)
    get_user_resp2 = client.get(user_url + "/" + auth_user_id, headers=headers)
    assert get_user_resp.json != get_user_resp2.json

    new_profile = {"description": "desc", "picture": None,
                   "primary_hue": 0.25, "primary_saturation": 0.4, "primary_lightness": 0.1,
                   "secondary_hue": 0.25, "secondary_saturation": 0.4, "secondary_lightness": 0.1}
    set_profile_resp = client.post(user_url + "/profile", headers=headers, json=new_profile)
    get_user_resp3 = client.get(user_url + "/" + auth_user_id, headers=headers)
    assert get_user_resp2.json != get_user_resp3.json
    print(get_user_resp.json)
    print(get_user_resp2.json)
    print(get_user_resp3.json)

    logout_resp = client.post(user_url + "/logout", headers=headers)
    assert logout_resp.status_code == 200


def test_user_interaction(client):
    reg_resp = client.post(user_url, json=user_one)
    reg_resp2 = client.post(user_url, json=user_two)
    assert reg_resp.status_code == 200 and reg_resp2.status_code == 200

    headers = {"Authorization": "Bearer " + reg_resp.json.get("auth_token"), "id": reg_resp.json.get("auth_user_id")}
    headers2 = {"Authorization": "Bearer " + reg_resp2.json.get("auth_token"), "id": reg_resp2.json.get("auth_user_id")}

    follow_resp = client.post(user_url + "/follow/" + headers2.get("id"), headers=headers)
    get_user_resp = client.get(user_url + "/" + headers.get("id"), headers=headers)
    assert get_user_resp.json.get("pending_following_count") == 1

    accept_follow_resp = client.post(user_url + "/followers/pending/" + headers.get("id") + "/accept", headers=headers2)
    get_user_resp = client.get(user_url + "/" + headers.get("id"), headers=headers)
    assert get_user_resp.json.get("following_count") == 1 and get_user_resp.json.get("pending_following_count") == 0

    block_resp = client.post(user_url + "/block/" + headers.get("id"), headers=headers2)
    get_user_resp = client.get(user_url + "/" + headers.get("id"), headers=headers)
    assert get_user_resp.json.get("following_count") == 0

    block_resp = client.delete(user_url + "/block/" + headers.get("id"), headers=headers2)
    assert block_resp.status_code == 200

    follow_resp = client.post(user_url + "/follow/" + headers2.get("id"), headers=headers)
    get_user_resp = client.get(user_url + "/" + headers.get("id"), headers=headers)
    assert get_user_resp.json.get("pending_following_count") == 1

    accept_follow_resp = client.post(user_url + "/followers/pending/" + headers.get("id") + "/reject", headers=headers2)
    get_user_resp = client.get(user_url + "/" + headers.get("id"), headers=headers)
    assert get_user_resp.json.get("following_count") == 0 and get_user_resp.json.get("pending_following_count") == 0


def test_post(client):
    reg_resp = client.post(user_url, json=user_one)
    assert reg_resp.status_code == 200

    headers = {"Authorization": "Bearer " + reg_resp.json.get("auth_token"), "id": reg_resp.json.get("auth_user_id")}

    post_dict = {"title": "title", "text": "text", "media": "media", "media_type": "image", "latitude": 1, "longitude": 1}
    post_post_resp = client.post(post_url, json=post_dict, headers=headers)
    assert post_post_resp.status_code == 200
    post_dict = post_post_resp.json

    like_resp = client.post(post_url + "/" + post_dict.get("id") + "/like", headers=headers)
    get_post_resp = client.get(post_url + "/" + post_dict.get("id"), headers=headers)
    assert get_post_resp.json.get("like_count") == 1

    unlike_resp = client.delete(post_url + "/" + post_dict.get("id") + "/like", headers=headers)
    get_post_resp = client.get(post_url + "/" + post_dict.get("id"), headers=headers)
    assert get_post_resp.json.get("like_count") == 0

    like_resp = client.post(post_url + "/" + post_dict.get("id") + "/like", headers=headers)
    get_post_resp = client.get(post_url + "/" + post_dict.get("id"), headers=headers)
    assert get_post_resp.json.get("like_count") == 1

    dislike_resp = client.post(post_url + "/" + post_dict.get("id") + "/dislike", headers=headers)
    get_post_resp = client.get(post_url + "/" + post_dict.get("id"), headers=headers)
    assert get_post_resp.json.get("like_count") == 0 and get_post_resp.json.get("dislike_count") == 1

    dislike_resp = client.delete(post_url + "/" + post_dict.get("id") + "/dislike", headers=headers)
    get_post_resp = client.get(post_url + "/" + post_dict.get("id"), headers=headers)
    assert get_post_resp.json.get("dislike_count") == 0
