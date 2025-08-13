package twitter.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import twitter.entity.post.Post;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "twitter_user")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "login")
    protected String login;

    @Column(name = "password")
    protected String password;

    @Column(name = "registered_at")
    protected LocalDateTime registrationDate;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    protected UserType userType;

    @OneToMany(mappedBy = "author")
    protected List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "m2m_user_likes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id")
    )
    protected List<Post> postsILike;

    protected User() {}

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPostsILike() {
        return postsILike;
    }

    public void setPostsILike(List<Post> postsILike) {
        this.postsILike = postsILike;
    }

    public abstract String beautify();

    public abstract String whatIsYourName();

    public abstract String toFileString();

    @Override
    public int hashCode() {
        return 31 * (this.id + this.login.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return id == user.id && login.equals(user.login);
    }
}
