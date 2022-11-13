package projects;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.dao.DbException;
import projects.entity.Project;
import projects.service.ProjectService;

public class ProjectsApp {
	
	private Project curProject;
	
	private ProjectService projectService = new ProjectService();
	
	
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project", 
			"2) List projects",
			"3) Select a project"
			);
	
	private Scanner kb = new Scanner(System.in);
	// @formatter:on
	public static void main(String[] args) {
		
		new ProjectsApp().processUserSelection();
	

	}
	private void processUserSelection() {
		boolean done = false;
		
		while(!done) {
			try {
				int selection = getUserSelection();
				
				switch(selection) {
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				default: 
					System.out.println("\n" + selection + " is not "
							+ "a valid selction.  Try again.");
				}
				
			} catch (Exception e) {
				
				System.out.println(e.toString());
			}
		}
		
	}
	
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project.");
		curProject = null;
		curProject = projectService.fetchProjectById(projectId);
		
	}
	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("   " + project.getProjectId()
		+ ": " + project.getProjectName()));
		
	}
	private void createProject() {
		String projectName = getStringInput("Enter a project name");
		BigDecimal estimatedHours = getDecimalInput("Enter estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		Project project = new Project();
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
		
	}
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid Decimal Number.  Try again.");
		}
	}
	private boolean exitMenu() {
		System.out.println("\nExiting the menu. TTFN!");
		return true;
	}
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu Selection");
		
		return Objects.isNull(input) ? -1 : input;
		
		
	}
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.  Try again.");
		}
	}
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = kb.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}
	private void printOperations() {
		System.out.println("\nThese are the available selections.  Press the enter key to quit.");
		operations.forEach(line -> System.out.println(" " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		}
		else {
			System.out.println("\nYou are working with a project.: " + curProject);
		}
	}

}
