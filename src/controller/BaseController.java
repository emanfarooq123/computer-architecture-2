package controller;

import persistence.Repository;

public abstract class BaseController {
    protected final Repository repo = Repository.get();
    protected final String folder;
    protected BaseController(String folder){ this.folder = folder; }

    protected void autosave() throws Exception { repo.saveAll(folder); }
    public void reloadAll() throws Exception { repo.loadAll(folder); }
}
