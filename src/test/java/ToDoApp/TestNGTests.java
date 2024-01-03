package ToDoApp;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestNGTests {

    private ToDoList toDoList;

    @BeforeMethod(groups = {"basic", "exception", "parallel"})
    public void setUp() {
        toDoList = new ToDoList();
    }

    @Test(groups = {"basic"})
    public void newTaskCanBeAdded() {
        Task task = new Task();
        task.setTitle("Homework");
        task.setProjectName("School");

        toDoList.addToDo(task);

        assertEquals(toDoList.getToDoList().size(), 1);
    }

    @Test(groups = {"exception"}, expectedExceptions = IndexOutOfBoundsException.class)
    public void removeTaskThrowsIndexOutOfBounds() {
        toDoList.remove(toDoList.getToDoList().size());
    }

    @Test(groups = {"basic"}, dependsOnMethods = {"newTaskCanBeAdded"})
    public void firstTaskCanBeRemoved() {
        Task task = new Task();
        task.setTitle("Homework");
        toDoList.addToDo(task);

        toDoList.remove(0);

        assertEquals(toDoList.getToDoList().size(), 0);
    }

    @Test(groups = {"basic"}, dependsOnMethods = {"newTaskCanBeAdded"})
    public void firstTaskCanBeRetrievedFromList() {
        Task task = new Task();
        task.setTitle("Homework");
        toDoList.addToDo(task);

        Task retrievedTask = toDoList.getTaskInToDo(0);

        assertEquals(retrievedTask.getTitle(), "Homework");
    }

    @Parameters({"projectName"})
    @Test(groups = {"parameterized"})
    public void projectNameParameterizedTest(String projectName) {
        Task task = new Task();
        task.setProjectName(projectName);

        assertEquals(task.getProjectName(), projectName);
    }

    @DataProvider(name = "taskTitles")
    public Object[][] createTaskData() {
        return new Object[][] {{"Homework"}, {"Shopping"}, {"Exercise"}};
    }

    @Test(groups = {"parameterized"}, dataProvider = "taskTitles")
    public void titleParameterizedTest(String title) {
        Task task = new Task();
        task.setTitle(title);

        assertEquals(task.getTitle(), title);
    }

    @Test(groups = {"parallel"}, threadPoolSize = 5, invocationCount = 5, timeOut = 1000)
    public void parallelTaskAdditionTest() {
        String taskTitle = "Task" + Math.random();
        Task task = new Task();
        task.setTitle(taskTitle);
        synchronized (toDoList) {
            toDoList.addToDo(task);
        }
        boolean isAdded = toDoList.getToDoList().stream().anyMatch(t -> t.getTitle().equals(taskTitle));
        assertTrue(isAdded);
    }
}
