package modules;

import dataTypes.GoalsList;
import fileIO.FileRead;
import fileIO.FileSave;
import uiComponents.Checklist;
import uiComponents.TextButton;
import uiComponents.TextInput;

import java.awt.*;

public class ChecklistModule extends manager.Module {

    public static final String defaultFilename = "test.txt";

    private GoalsList list;


    public ChecklistModule(String name) {
        super(name, "/checklist/");
        Checklist checklist = new Checklist(400, 200, "Test");
        components.add(checklist);
        GoalsList goalsList = new GoalsList();
        goalsList.add("Test 1", false);
        goalsList.add("test 56", false);
        goalsList.add("5 91 t", true);
        this.list = goalsList;

        goalsList.loadIntoChecklist(checklist);

        TextInput fileIn = new TextInput(50, 10, 200, 40, defaultFilename, c -> System.out.println("Activated"));

        components.add(fileIn);

        components.add(new TextButton(50, 80, 250, 50, "Load data", Color.RED, new Color(250, 100, 100), Color.BLACK, (b) ->
        {
            System.out.println("Loading file...");
            list = FileRead.readGoalsListFile(modulePath + ((TextInput) b.targets[1]).builder.toString());

            Checklist c = (Checklist) b.targets[0];

            list.loadIntoChecklist(c);

            System.out.println("Done");
        }, checklist, fileIn));

        components.add(
            new TextButton(50, 180, 250, 50, "Save data", Color.GREEN, new Color(100, 225, 100), Color.BLACK, (b) ->
            {
                System.out.println("Saving file...");

                list.loadFromChecklist((Checklist) b.targets[0]);

                FileSave.saveFile(list, modulePath + ((TextInput) b.targets[1]).builder.toString());

                System.out.println("Done");
            }, checklist, fileIn));

    }


}
