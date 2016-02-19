package com.lwzh.action;

import java.io.File;
import java.io.IOException;

import com.lwzh.tool.IdTool;
import com.lwzh.web.BaseAction;

public class TestAction extends BaseAction {

	public void test_export() throws IOException {
		File rootDir = FileSystemAction.getRootDir(getContext());
		File fsRootDir = FileSystemAction.getFsRootDir(getContext());

		File saveFile = new File(fsRootDir, "upload/pdf/" + IdTool.uuid() + ".pdf").getCanonicalFile();
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		System.out.println(saveFile);
	}

	public void dialog() {
	}

	public void file_upload() {
	}

	public void area_select() {
	}

	public void test_validate() {
	}

	public void test_remote() {
		setRespData(1, null, null);
		return;
	}

	public void gather_data() {
	}

}
