package io.onedev.server.plugin.imports.github;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import io.onedev.server.util.validation.annotation.ProjectName;
import io.onedev.server.web.editable.annotation.Editable;

@Editable
public class GitHubImport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROP_ONEDEV_PROJECT = "oneDevProject";
	
	private String gitHubRepo;
	
	private String oneDevProject;
	
	private boolean importIssues = true;
	
	@Editable(order=100, name="GitHub Repository", description="Specify GitHub repository to import "
			+ "code and issues (optional) from")
	@NotEmpty
	public String getGitHubRepo() {
		return gitHubRepo;
	}

	public void setGitHubRepo(String gitHubRepo) {
		this.gitHubRepo = gitHubRepo;
	}

	@Editable(order=200, name="OneDev Project", description="Specify OneDev project to be created as "
			+ "result of importing")
	@ProjectName
	@NotEmpty
	public String getOneDevProject() {
		return oneDevProject;
	}

	public void setOneDevProject(String oneDevProject) {
		this.oneDevProject = oneDevProject;
	}

	@Editable(order=400)
	public boolean isImportIssues() {
		return importIssues;
	}

	public void setImportIssues(boolean importIssues) {
		this.importIssues = importIssues;
	}

}