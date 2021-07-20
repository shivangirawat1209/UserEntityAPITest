package RestPojoClasses;

import java.util.List;

public class PostResponsePayload {

    private int id;
    private String firstName ;
    private String lastName;
    private String email;
    private String dayOfBirth;
    private List<Content> content;
    private List<Links> links;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public void setLinks(List<Links> links) {
        this.links = links;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", content=" + content +
                ", links=" + links +
                '}';
    }
}
