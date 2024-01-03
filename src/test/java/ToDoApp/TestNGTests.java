package ToDoApp;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestNGTests {
    private ToDoList toDoList;
    private FileHandler fileHandler;

    @BeforeMethod(groups = {"file-operations", "task-manipulations", "task-search", "task-status", "parallel"})
    public void setUp() {
        toDoList = new ToDoList();
        fileHandler = new FileHandler();
    }

    // Group: File Operations
    @Test(groups = {"file-operations"}, priority = 1)
    public void testSaveAndLoadTasks() throws IOException, ClassNotFoundException {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setProjectName("Test Project");
        task.setDueDate(new Date());
        task.setStatus(Status.PENDING);
        toDoList.addToDo(task);

        fileHandler.saveToFile(toDoList.getToDoList());
        List<Task> loadedTasks = fileHandler.loadFromFile();

        assertNotNull(loadedTasks);
        assertEquals(loadedTasks.size(), 1);
        assertEquals(loadedTasks.get(0).getTitle(), "Test Task");
    }

    // Group: Task Manipulations
    @Test(groups = {"task-manipulations"}, priority = 2)
    public void testAddTask() {
        Task task = new Task();
        task.setTitle("New Task");
        toDoList.addToDo(task);
        assertEquals(toDoList.getToDoList().size(), 1);
    }

    @Test(groups = {"task-manipulations"}, dependsOnMethods = {"testAddTask"}, priority = 2)
    public void testRemoveTask() {
        Task task = new Task();
        task.setTitle("New Task");
        toDoList.addToDo(task);
        toDoList.remove(0);
        assertEquals(toDoList.getToDoList().size(), 0);
    }

    @Test(groups = {"task-manipulations"}, expectedExceptions = IndexOutOfBoundsException.class, priority = 2)
    public void testRemoveTaskWithInvalidIndex() {
        toDoList.remove(1);
    }

    @DataProvider(name = "taskTitles")
    public Object[][] createTaskTitles() {
        return new Object[][] {
                {"Groceries"},
                {"Homework"},
                {"Gym"}
        };
    }

    @Test(groups = {"task-manipulations"}, dataProvider = "taskTitles", priority = 3)
    public void testAddTaskWithVariousTitles(String title) {
        Task task = new Task();
        task.setTitle(title);
        toDoList.addToDo(task);
        assertEquals(toDoList.getTaskInToDo(toDoList.getToDoList().size() - 1).getTitle(), title);
    }

    @Parameters({"due-dates"})
    @Test(groups = {"task-manipulations"})
    public void setProjectDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date date = sdf.parse(dateString);
        Task task = new Task();
        task.setDueDate(date);

        assertEquals(task.getDueDate(), date);
    }

    // Group: Task Search
    @Test(groups = {"task-search"}, priority = 3)
    public void testSearchTaskByTitle() {
        Task task = new Task();
        task.setTitle("Meeting");
        toDoList.addToDo(task);

        List<Task> foundTasks = toDoList.searchTasksByTitle("Meeting");
        assertFalse(foundTasks.isEmpty());
        assertEquals(foundTasks.get(0).getTitle(), "Meeting");
    }

    @Test(groups = {"task-search"}, priority = 3)
    public void testSearchNonExistingTask() {
        List<Task> foundTasks = toDoList.searchTasksByTitle("NonExisting");
        assertTrue(foundTasks.isEmpty());
    }

    // Group: Task Status
    @Test(groups = {"task-status"}, priority = 3)
    public void testMarkTaskAsCompleted() {
        Task task = new Task();
        task.setStatus(Status.DONE);
        toDoList.addToDo(task);

        assertEquals(toDoList.getTaskInToDo(0).getStatus(), Status.DONE);
    }

    @Test(groups = {"task-status"}, priority = 3)
    public void testCountCompletedTasks() {
        Task task1 = new Task();
        task1.setStatus(Status.DONE);
        toDoList.addToDo(task1);

        Task task2 = new Task();
        task2.setStatus(Status.PENDING);
        toDoList.addToDo(task2);

        long completedCount = toDoList.getToDoList().stream()
                .filter(t -> t.getStatus() == Status.DONE)
                .count();
        assertEquals(completedCount, 1);
    }

    @Test(groups = {"parallel"}, threadPoolSize = 5, invocationCount = 5, timeOut = 1000, priority = 4)
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
