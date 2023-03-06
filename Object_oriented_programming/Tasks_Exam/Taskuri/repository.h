#pragma once
#include "domain.h"

class RepoTask
{
private:

	string file;
	vector <Task> taskuri;
	void citesteDinFisier();
	void incarcaInFisier();


public:

	RepoTask(string f) : file{ f } { citesteDinFisier(); }

	vector <Task> getAll();
	void adauga(Task t);
	vector<Task> cauta(string nume);
	void update(int id, string stare);

};