package come.lc.df.studentApp.studentinfo;

import come.lc.df.studentApp.model.StudentPojo;
import come.lc.df.studentApp.testbase.TestBase;
import come.lc.df.studentApp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentCrudTest extends TestBase {
    static String firstName = "Apurva" + TestUtils.getRandomValue();
    static String lastName = "Patel" + TestUtils.getRandomValue();
    static String programme = "Software Testing";
    static String email = TestUtils.getRandomValue() + "xyz@yahoo.com";
    static int studentId;


    @Title("New Student will be added")
    @Test
    public void test001() {
        List<String> courses = new ArrayList<>();
        courses.add("Manual");
        courses.add("Automation");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .post()
                .then().log().all().statusCode(201);

    }

    @Title("Verify if the student was added to the application")
    @Test
    public void test002() {
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        HashMap<String, Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        assertThat(value, hasValue(firstName));
        studentId = (int) value.get("id");
    }
    @Title("Update the user information and verify the updated information")
    @Test
    public void test03() {
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        firstName = firstName +"_Updated";

        List<String> courses = new ArrayList<>();
        courses.add("Rest Assured");
        courses.add("Selenium");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .put("/"+studentId)
                .then().log().all().statusCode(200);

        HashMap<String, Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        System.out.println(value);
        assertThat(value, hasValue(firstName));


    }
    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test04() {
        SerenityRest.rest()
                .given()
                .when()
                .delete("/"+studentId)
                .then()
                .statusCode(204);

        SerenityRest.rest()
                .given()
                .when()
                .get("/"+studentId)
                .then().statusCode(404);
    }
}
