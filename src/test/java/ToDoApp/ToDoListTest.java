package ToDoApp;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.ArrayList;
import java.util.List;



public class ToDoListTest {


    @Test
    public void newTaskCanBeAdded() {
        ToDoList toDoList = new ToDoList();
        Task task = new Task();
        task.setTitle("Homework");
        task.setProjectName("School");

        final int initialSize = toDoList.getToDoList().size();

        toDoList.addToDo(task);

        Assert.assertEquals(toDoList.getToDoList().size(), initialSize + 1);
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void removeTaskThrowsIndexOutOfBounds() {
        ToDoList list = new ToDoList();
        list.remove(list.getToDoList().size()); // Directly perform the action that should throw the exception
    }

    @Test
    public void FirstTaskCanBeRemoved() {
        ToDoList toDoList = new ToDoList();

        Task task = new Task();
        task.setTitle("Homework");
        task.setProjectName("School");
        toDoList.addToDo(task);

        final int initialSize = toDoList.getToDoList().size();

        toDoList.remove(0);

        Assert.assertEquals(toDoList.getToDoList().size(), initialSize - 1);

    }

    @Test
    public void FirstTaskCanBeRetrievedFromList() {
        ToDoList toDoList = new ToDoList();

        Task task = new Task();
        task.setTitle("Homework");
        task.setProjectName("School");
        toDoList.addToDo(task);

        final Task retrievedFirstTask = toDoList.getTaskInToDo(0);

        Assert.assertEquals(retrievedFirstTask, task);
    }

    @Test
    public void ListCanBeRetrieved() {
        List<Task> list = new ArrayList<Task>();
        ToDoList toDoList = new ToDoList();

        toDoList.setToDoList(list);

        final List<Task> retrievedList = toDoList.getToDoList();

        Assert.assertEquals(retrievedList, list);
    }

}