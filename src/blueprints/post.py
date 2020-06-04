from flask import Blueprint, request, jsonify
from flask_jwt_extended import get_jwt_identity, jwt_required
from database import User, Post, Comment
from extensions import db

bp = Blueprint("post", __name__, url_prefix="/post")
items_per_page = 20  # Applies to comments


@bp.route("/all/<radius>", methods=["GET"])
@jwt_required
def get_posts(radius):
    if request.method != "GET":
        return "", 405

    user = User.query.get(get_jwt_identity())

    try:
        radius = float(radius)
    except Exception:
        return "", 400

    min_latitude = user.latitude - radius
    max_latitude = user.latitude + radius
    min_longitude = user.longitude - radius
    max_longitude = user.longitude + radius

    posts = Post.query.filter((Post.latitude > min_latitude) & (Post.latitude < max_latitude) &
                              (Post.longitude > min_longitude) & (Post.longitude < max_longitude)).all()
    return jsonify([x.to_dict(user) for x in posts])


@bp.route("/<post_id>", methods=["GET"])
@jwt_required
def get_post(post_id):
    if request.method != "GET":
        return "", 405

    self = User.query.get(get_jwt_identity())
    post = Post.query.get_or_404(post_id)
    return jsonify(post.to_dict(self))


@bp.route("", methods=["POST"])
@jwt_required
def post_post():
    if request.method != "POST":
        return "", 405

    author = User.query.get(get_jwt_identity())
    title = request.json.get("title")
    text = request.json.get("text")
    media = request.json.get("media")
    media_type = request.json.get("media_type")
    latitude = request.json.get("latitude")
    longitude = request.json.get("longitude")

    post = Post.new(author, title, text, media, media_type, latitude, longitude)
    db.session.commit()
    return jsonify(post.to_dict(author))


@bp.route("/<post_id>/like", methods=["POST", "DELETE"])
@jwt_required
def like_post(post_id):
    if request.method not in ["POST", "DELETE"]:
        return "", 405

    user = User.query.get(get_jwt_identity())
    post = Post.query.get_or_404(post_id)
    if request.method == "POST":
        user.like_post(post)
    elif request.method == "DELETE":
        user.unlike_post(post)
    db.session.commit()
    return jsonify(post.to_dict(user))


@bp.route("/<post_id>/dislike", methods=["POST", "DELETE"])
@jwt_required
def dislike_post(post_id):
    if request.method not in ["POST", "DELETE"]:
        return "", 405

    user = User.query.get(get_jwt_identity())
    post = Post.query.get_or_404(post_id)
    if request.method == "POST":
        user.dislike_post(post)
    elif request.method == "DELETE":
        user.undislike_post(post)
    db.session.commit()
    return jsonify(post.to_dict(user))


@bp.route("/<post_id>/comment", methods=["POST"])
@jwt_required
def post_comment(post_id):
    if request.method != "POST":
        return "", 405

    author = User.query.get(get_jwt_identity())
    post = Post.query.get_or_404(post_id)
    text = request.json.get("text")

    user = User.query.get(get_jwt_identity())
    comment = Comment.new(author, post, text)
    db.session.commit()
    return jsonify(comment.to_dict(user))


@bp.route("/<post_id>/comments/<page>", methods=["GET"])
@jwt_required
def get_comments(post_id, page):
    if request.method != "GET":
        return "", 405

    user = User.query.get(get_jwt_identity())
    post = User.query.get_or_404(post_id)
    comments = post.comments.offset(page * items_per_page).limit(items_per_page).all()
    return jsonify([x.to_dict(user) for x in comments])
