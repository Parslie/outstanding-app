import uuid
import datetime
from extensions import db, bcrypt


def generate_id(table_query):
    while True:
        new_id = uuid.uuid4().hex

        if not table_query.filter_by(id=new_id).first():
            return new_id


#####################
# Relationship tables
pending_followings = db.Table("pending_followings",
                              db.Column("follower_id", db.String, db.ForeignKey("user.id"), primary_key=True),
                              db.Column("followee_id", db.String, db.ForeignKey("user.id"), primary_key=True))
followings = db.Table("followings",
                      db.Column("follower_id", db.String, db.ForeignKey("user.id"), primary_key=True),
                      db.Column("followee_id", db.String, db.ForeignKey("user.id"), primary_key=True))
blockings = db.Table("blockings",
                     db.Column("blocker_id", db.String, db.ForeignKey("user.id"), primary_key=True),
                     db.Column("blockee_id", db.String, db.ForeignKey("user.id"), primary_key=True))

liked_posts = db.Table("liked_posts",
                       db.Column("user_id", db.String, db.ForeignKey("user.id"), primary_key=True),
                       db.Column("post_id", db.String, db.ForeignKey("post.id"), primary_key=True))
disliked_posts = db.Table("disliked_posts",
                          db.Column("user_id", db.String, db.ForeignKey("user.id"), primary_key=True),
                          db.Column("post_id", db.String, db.ForeignKey("post.id"), primary_key=True))


########
# Tables
class User(db.Model):
    id = db.Column(db.String, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    email = db.Column(db.String, unique=True, nullable=False)
    hashword = db.Column(db.String, nullable=False)

    latitude = db.Column(db.Float, nullable=False, default=1)
    longitude = db.Column(db.Float, nullable=False, default=1)
    date_created = db.Column(db.DateTime, nullable=False)
    is_online = db.Column(db.Boolean, nullable=False, default=True)

    profile = db.relationship("Profile", backref="author", uselist=False)  # One-to-one

    posts = db.relationship("Post", lazy="dynamic", backref="author")  # One-to-many
    comments = db.relationship("Comment", lazy="dynamic", backref="author")  # One-to-many

    pending_followings = db.relationship("User", secondary=pending_followings, lazy="dynamic",  # Many-to-many
                                         primaryjoin=(pending_followings.c.follower_id == id),
                                         secondaryjoin=(pending_followings.c.followee_id == id),
                                         backref=db.backref("pending_followers", lazy="dynamic"))
    followings = db.relationship("User", secondary=followings, lazy="dynamic",  # Many-to-many
                                 primaryjoin=(followings.c.follower_id == id),
                                 secondaryjoin=(followings.c.followee_id == id),
                                 backref=db.backref("followers", lazy="dynamic"))
    blockings = db.relationship("User", secondary=blockings, lazy="dynamic",  # Many-to-many
                                primaryjoin=(blockings.c.blocker_id == id),
                                secondaryjoin=(blockings.c.blockee_id == id),
                                backref=db.backref("blockers", lazy="dynamic"))

    liked_posts = db.relationship("Post", secondary=liked_posts, lazy="dynamic",  # Many-to-many
                                  backref=db.backref("likers", lazy="dynamic"))
    disliked_posts = db.relationship("Post", secondary=disliked_posts, lazy="dynamic",  # Many-to-many
                                     backref=db.backref("dislikers", lazy="dynamic"))

    @staticmethod
    def new(username, email, password):
        user = User(username, email, password)
        db.session.add(user)
        return user

    def __init__(self, username, email, password):
        self.id = generate_id(User.query)
        self.username = username
        self.email = email
        self.hashword = bcrypt.generate_password_hash(password).decode("utf-8")
        self.profile = Profile.new(self)
        self.date_created = datetime.datetime.now()

    def to_dict(self, comparer):
        return {"id": self.id, "username": self.username, "email": self.email,
                "profile": self.profile.to_dict(), "date_created": self.date_created,
                "latitude": self.latitude, "longitude": self.longitude, "is_online": self.is_online,
                "follower_count": self.followers.count(), "following_count": self.followings.count(),
                "pending_follower_count": self.pending_followers.count(),
                "pending_following_count": self.pending_followings.count(),
                "is_following": comparer is not self and comparer.followings.filter_by(id=comparer.id).first() is not None}

    # Actions
    def like_post(self, post):
        if self.disliked_posts.filter_by(id=post.id).first():
            self.disliked_posts.remove(post)
        if not self.liked_posts.filter_by(id=post.id).first():
            self.liked_posts.append(post)

    def dislike_post(self, post):
        if self.liked_posts.filter_by(id=post.id).first():
            self.liked_posts.remove(post)
        if not self.disliked_posts.filter_by(id=post.id).first():
            self.disliked_posts.append(post)

    def unlike_post(self, post):
        if self.liked_posts.filter_by(id=post.id).first():
            self.liked_posts.remove(post)

    def undislike_post(self, post):
        if self.disliked_posts.filter_by(id=post.id).first():
            self.disliked_posts.remove(post)

    def follow(self, user):
        if not self.pending_followings.filter_by(id=user.id).first() and not self.followings.filter_by(id=user.id).first():
            self.pending_followings.append(user)

    def unfollow(self, user):
        if self.pending_followings.filter_by(id=user.id).first():
            self.pending_followings.remove(user)
        if self.followings.filter_by(id=user.id).first():
            self.followings.remove(user)

    def accept_follow(self, user):
        if self.pending_followers.filter_by(id=user.id).first():
            self.pending_followers.remove(user)
            self.followers.append(user)

    def reject_follow(self, user):
        if self.pending_followers.filter_by(id=user.id).first():
            self.pending_followers.remove(user)

    def block(self, user):
        if not self.blockings.filter_by(id=user.id).first():
            self.blockings.append(user)
        self.reject_follow(user)
        self.unfollow(user)
        user.reject_follow(self)
        user.unfollow(self)

    def unblock(self, user):
        if self.blockings.filter_by(id=user.id).first():
            self.blockings.remove(user)


class Profile(db.Model):
    id = db.Column(db.String, primary_key=True)
    description = db.Column(db.String(200), nullable=False, default="")
    picture = db.Column(db.String, nullable=True)

    primary_hue = db.Column(db.Float, nullable=False, default=1)
    primary_saturation = db.Column(db.Float, nullable=False, default=0)
    primary_lightness = db.Column(db.Float, nullable=False, default=1)
    secondary_hue = db.Column(db.Float, nullable=False, default=1)
    secondary_saturation = db.Column(db.Float, nullable=False, default=0)
    secondary_lightness = db.Column(db.Float, nullable=False, default=0)

    author_id = db.Column(db.String, db.ForeignKey("user.id"), nullable=False)

    @staticmethod
    def new(author):
        profile = Profile(author)
        db.session.add(profile)
        return profile

    def __init__(self, author):
        self.id = generate_id(Profile.query)
        self.author = author

    def to_dict(self):
        return {"id": self.id, "description": self.description, "picture": self.picture,
                "primary_hue": self.primary_hue, "primary_saturation": self.primary_saturation,
                "primary_lightness": self.primary_lightness, "secondary_hue": self.secondary_hue,
                "secondary_saturation": self.secondary_saturation, "secondary_lightness": self.secondary_lightness}

    # Setters
    def set_primary_color(self, hue, saturation, lightness):
        self.primary_hue = hue
        self.primary_saturation = saturation
        self.primary_lightness = lightness

    def set_secondary_color(self, hue, saturation, lightness):
        self.secondary_hue = hue
        self.secondary_saturation = saturation
        self.secondary_lightness = lightness


class Post(db.Model):
    id = db.Column(db.String, primary_key=True)
    title = db.Column(db.String(40), nullable=True)
    text = db.Column(db.String(400), nullable=True)

    media = db.Column(db.String, nullable=True)
    media_type = db.Column(db.String, nullable=False)

    latitude = db.Column(db.Float, nullable=False)
    longitude = db.Column(db.Float, nullable=False)
    date_created = db.Column(db.DateTime, nullable=False)

    author_id = db.Column(db.String, db.ForeignKey("user.id"), nullable=False)  # Many-to-one
    comments = db.relationship("Comment", lazy="dynamic", backref="post")  # One-to-many

    @staticmethod
    def new(author, title, text, media, media_type, latitude, longitude):
        post = Post(author, title, text, media, media_type, latitude, longitude)
        db.session.add(post)
        return post

    def __init__(self, author, title, text, media, media_type, latitude, longitude):
        self.id = generate_id(Post.query)
        self.author = author
        self.title = title
        self.text = text
        self.media = media
        self.media_type = media_type
        self.latitude = latitude
        self.longitude = longitude
        self.date_created = datetime.datetime.now()

    def to_dict(self, author_comparer):
        return {"id": self.id, "author": self.author.to_dict(author_comparer),
                "title": self.title, "text": self.text,
                "media": self.media, "media_type": self.media_type,
                "latitude": self.latitude, "longitude": self.longitude, "date_created": self.date_created,
                "like_count": self.likers.count(), "dislike_count": self.dislikers.count(),
                "is_liked": author_comparer.liked_posts.filter_by(id=self.id).first() is not None,
                "is_disliked": author_comparer.disliked_posts.filter_by(id=self.id).first() is not None}


class Comment(db.Model):
    id = db.Column(db.String, primary_key=True)
    text = db.Column(db.String(200))
    date_created = db.Column(db.DateTime, nullable=False)

    author_id = db.Column(db.String, db.ForeignKey("user.id"), nullable=False)  # Many-to-one
    post_id = db.Column(db.String, db.ForeignKey("post.id"), nullable=False)  # Many-to-one

    @staticmethod
    def new(author, post, text):
        comment = Comment(author, post, text)
        db.session.add(comment)
        return comment

    def __init__(self, author, post, text):
        self.id = generate_id(Comment.query)
        self.author = author
        self.post = post
        self.text = text
        self.date_created = datetime.datetime.now()

    def to_dict(self, author_comparer):
        return {"id": self.id, "author": self.author.to_dict(author_comparer), "post": self.post.to_dict(author_comparer),
                "text": self.text, "date_created": self.date_created}


class BlacklistedToken(db.Model):
    jti = db.Column(db.String, primary_key=True)

    @staticmethod
    def new(jti):
        token = BlacklistedToken(jti)
        db.session.add(token)
        return token

    def __init__(self, jti):
        self.jti = jti
