from flask import Blueprint, request, jsonify
from flask_jwt_extended import create_access_token, get_jwt_identity, get_raw_jwt, jwt_required
from database import User, BlacklistedToken
from extensions import bcrypt, db

bp = Blueprint("user", __name__, url_prefix="/user")
items_per_page = 20  # Applies to posts, followers & followings


@bp.route("", methods=["POST"])
def register_user():
    if request.method != "POST":
        return "", 405

    username = request.json.get("username")
    email = request.json.get("email")
    password = request.json.get("password")
    password_confirmation = request.json.get("password_confirmation")

    if not username or not email or not password:
        return "", 400
    if password != password_confirmation:
        return "", 403

    if User.query.filter_by(username=username).first() or User.query.filter_by(email=email).first():
        return "", 409

    user = User.new(username, email, password)
    db.session.commit()
    auth_token = create_access_token(user.id)  # TODO: add good expiry timedelta
    return jsonify({"auth_token": auth_token, "auth_user_id": user.id})


@bp.route("/login", methods=["POST"])
def login_user():
    if request.method != "POST":
        return "", 405

    username = request.json.get("username")
    password = request.json.get("password")

    if not username or not password:
        return "", 400

    user = User.query.filter_by(username=username).first()
    if user is None or not bcrypt.check_password_hash(user.hashword, password):
        return "", 403

    auth_token = create_access_token(user.id)  # TODO: add good expiry timedelta
    return jsonify({"auth_token": auth_token, "auth_user_id": user.id})


@bp.route("/logout", methods=["POST"])
@jwt_required
def logout_user():
    if request.method != "POST":
        return "", 405

    jti = get_raw_jwt()["jti"]
    BlacklistedToken.new(jti)
    return ""


@bp.route("/online", methods=["POST", "DELETE"])
@jwt_required
def set_online():
    if request.method not in ["POST", "DELETE"]:
        return "", 405

    user = User.query.get(get_jwt_identity())
    user.is_online = request.method == "POST"
    db.session.commit()
    return ""


@bp.route("/coordinates", methods=["POST"])
@jwt_required
def set_coordinates():
    if request.method != "POST":
        return "", 405

    latitude = request.json.get("latitude")
    longitude = request.json.get("longitude")

    if latitude is None or longitude is None:
        return "", 400

    user = User.query.get(get_jwt_identity())
    user.latitude = latitude
    user.longitude = longitude
    db.session.commit()
    return ""


@bp.route("/profile", methods=["POST"])
@jwt_required
def set_profile():
    if request.method != "POST":
        return "", 405

    description = request.json.get("description")
    picture = request.json.get("picture")
    primary_hue = request.json.get("primary_hue")
    primary_saturation = request.json.get("primary_saturation")
    primary_lightness = request.json.get("primary_lightness")
    secondary_hue = request.json.get("secondary_hue")
    secondary_saturation = request.json.get("secondary_saturation")
    secondary_lightness = request.json.get("secondary_lightness")

    if description is None or \
            primary_hue is None or primary_saturation is None or primary_lightness is None or \
            secondary_hue is None or secondary_saturation is None or secondary_lightness is None:
        return "", 400

    user = User.query.get(get_jwt_identity())
    profile = user.profile
    profile.description = description
    profile.picture = picture
    profile.set_primary_color(primary_hue, primary_saturation, primary_lightness)
    profile.set_secondary_color(secondary_hue, secondary_saturation, secondary_lightness)
    db.session.commit()
    return ""


@bp.route("/account", methods=["POST"])
@jwt_required
def set_account():
    if request.method != "POST":
        return "", 405
    user = User.query.get(get_jwt_identity())

    username = request.json.get("username")
    email = request.json.get("email")
    password = request.json.get("password")

    if not username or not email:
        return "", 400

    username_user = User.query.filter_by(username=username).first()
    email_user = User.query.filter_by(email=email).first()
    if username_user is not None and username_user is not user or email_user is not None and email_user is not user:
        return "", 409

    user.username = username
    user.email = email
    if password:
        user.hashword = bcrypt.generate_password_hash(password).decode("utf-8")
    db.session.commit()
    return ""


@bp.route("/<user_id>", methods=["GET"])
@jwt_required
def get_user(user_id):
    if request.method != "GET":
        return "", 405

    self = User.query.get(get_jwt_identity())
    user = User.query.get_or_404(user_id)
    return jsonify(user.to_dict(self))


@bp.route("/followings/online", methods=["GET"])
@jwt_required
def get_online_followings():
    if request.method != "GET":
        return "", 405

    user = User.query.get(get_jwt_identity())
    followings = user.followings.filter_by(is_online=True).all()
    return jsonify([x.to_dict(user) for x in followings])


@bp.route("/<user_id>/followers/<page>", methods=["GET"])
@jwt_required
def get_followers(user_id, page):
    if request.method != "GET":
        return "", 405

    self = User.query.get(get_jwt_identity())
    user = User.query.get_or_404(user_id)
    followers = user.followers.offset(page * items_per_page).limit(items_per_page).all()
    return jsonify([x.to_dict(self) for x in followers])


@bp.route("/<user_id>/followings/<page>", methods=["GET"])
@jwt_required
def get_followings(user_id, page):
    if request.method != "GET":
        return "", 405

    self = User.query.get(get_jwt_identity())
    user = User.query.get_or_404(user_id)
    followings = user.followings.offset(page * items_per_page).limit(items_per_page).all()
    return jsonify([x.to_dict(self) for x in followings])


@bp.route("/followers/pending/<page>", methods=["GET"])
@jwt_required
def get_pending_followers(page):
    if request.method != "GET":
        return "", 405

    user = User.query.get(get_jwt_identity())
    pending_followers = user.pending_followers.offset(page * items_per_page).limit(items_per_page).all()
    return jsonify([x.to_dict(user) for x in pending_followers])


@bp.route("/followings/pending/<page>", methods=["GET"])
@jwt_required
def get_pending_followings(page):
    if request.method != "GET":
        return "", 405

    user = User.query.get(get_jwt_identity())
    pending_followings = user.pending_followings.offset(page * items_per_page).limit(items_per_page).all()
    return jsonify([x.to_dict(user) for x in pending_followings])


@bp.route("/<user_id>/posts/<page>", methods=["GET"])
@jwt_required
def get_posts(user_id, page):
    if request.method != "GET":
        return "", 405

    user = User.query.get_or_404(user_id)
    posts = user.posts.offset(page * items_per_page).limit(items_per_page).all()
    return jsonify([x.to_dict(user) for x in posts])


@bp.route("/blockings/<page>", methods=["GET"])
@jwt_required
def get_blockings(page):
    if request.method != "GET":
        return "", 405

    user = User.query.get(get_jwt_identity())
    blockings = user.blockings.offset(page * items_per_page).limit(items_per_page).all()
    return jsonify([x.to_dict(user) for x in blockings])


@bp.route("/follow/<user_id>", methods=["POST", "DELETE"])
@jwt_required
def set_follow(user_id):
    if request.method not in ["POST", "DELETE"]:
        return "", 405

    followee = User.query.get_or_404(user_id)
    user = User.query.get(get_jwt_identity())

    if user == followee:
        return "", 400

    if request.method == "POST":
        user.follow(followee)
    elif request.method == "DELETE":
        user.unfollow(followee)
    db.session.commit()
    return ""


@bp.route("/followers/pending/<user_id>/accept", methods=["POST"])
@jwt_required
def accept_pending_follower(user_id):
    if request.method != "POST":
        return "", 405

    follower = User.query.get_or_404(user_id)
    user = User.query.get(get_jwt_identity())
    user.accept_follow(follower)
    db.session.commit()
    return ""


@bp.route("/followers/pending/<user_id>/reject", methods=["POST"])
@jwt_required
def reject_pending_follower(user_id):
    if request.method != "POST":
        return "", 405

    follower = User.query.get_or_404(user_id)
    user = User.query.get(get_jwt_identity())
    user.reject_follow(follower)
    db.session.commit()
    return ""


@bp.route("/block/<user_id>", methods=["POST", "DELETE"])
@jwt_required
def set_block(user_id):
    if request.method not in ["POST", "DELETE"]:
        return "", 405

    blockee = User.query.get_or_404(user_id)
    user = User.query.get(get_jwt_identity())

    if user == blockee:
        return "", 400

    if request.method == "POST":
        user.block(blockee)
    elif request.method == "DELETE":
        user.unblock(blockee)
    db.session.commit()
    return ""
