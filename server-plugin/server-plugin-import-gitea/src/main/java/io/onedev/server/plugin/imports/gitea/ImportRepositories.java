package io.onedev.server.plugin.imports.gitea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;

import org.apache.shiro.authz.UnauthorizedException;

import io.onedev.server.OneDev;
import io.onedev.server.entitymanager.ProjectManager;
import io.onedev.server.model.Project;
import io.onedev.server.security.SecurityUtils;
import io.onedev.server.validation.Validatable;
import io.onedev.server.annotation.ClassValidating;
import io.onedev.server.annotation.Editable;
import io.onedev.server.annotation.OmitName;

@Editable
@ClassValidating
public class ImportRepositories implements Serializable, Validatable {

	private static final long serialVersionUID = 1L;

	private List<ProjectMapping> projectMappings = new ArrayList<>();
	
	@Editable(order=100, name="Repositories to Import")
	@Size(min=1, max=10000, message="No repositories to import")
	@OmitName
	public List<ProjectMapping> getProjectMappings() {
		return projectMappings;
	}

	public void setProjectMappings(List<ProjectMapping> projectMappings) {
		this.projectMappings = projectMappings;
	}

	@Override
	public boolean isValid(ConstraintValidatorContext context) {
		boolean isValid = true;
		ProjectManager projectManager = OneDev.getInstance(ProjectManager.class);
		for (int i=0; i<projectMappings.size(); i++) {
			try {
				Project project = projectManager.setup(projectMappings.get(i).getOneDevProject());
				if (!project.isNew() && !SecurityUtils.canManage(project))
					throw new UnauthorizedException("Project management permission is required");
				for (int j=0; j<projectMappings.size(); j++) {
					if (j != i && projectMappings.get(j).getOneDevProject().equals(projectMappings.get(i).getOneDevProject())) {
						context.buildConstraintViolationWithTemplate("Duplicate project")
								.addPropertyNode("projectMappings")
								.addPropertyNode(ProjectMapping.PROP_ONEDEV_PROJECT)
								.inIterable().atIndex(i).addConstraintViolation();
						isValid = false;
						break;
					}
				}
			} catch (UnauthorizedException e) {
				context.buildConstraintViolationWithTemplate(e.getMessage())
						.addPropertyNode("projectMappings")
						.addPropertyNode(ProjectMapping.PROP_ONEDEV_PROJECT)
						.inIterable().atIndex(i).addConstraintViolation();
				isValid = false;
			}
		}
		
		if (!isValid)
			context.disableDefaultConstraintViolation();
		return isValid;
	}

}
