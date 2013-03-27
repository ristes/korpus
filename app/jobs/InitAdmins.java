package jobs;

import models.Role;
import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class InitAdmins extends Job {

	@Override
	public void doJob() throws Exception {
		if (User.findAll().size() == 0) {
			System.out.println("Initializing admin user");

			Role admin = new Role();
			admin.name = "admin";
			admin.code = 1;
			admin.save();
			Role editor = new Role();
			editor.name = "editor";
			editor.code = 2;
			editor.save();
			User administrator = new User();
			administrator.username = "adimistrator";
			administrator.password = "@d^^1N";
			administrator.role = admin;
			administrator.save();
		}
		super.doJob();
	}

}
