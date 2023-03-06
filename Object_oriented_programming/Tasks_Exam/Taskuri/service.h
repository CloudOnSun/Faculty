#pragma once
#include "repository.h"
#include <algorithm>
class SrvTask
{
private:

	RepoTask& repo;

public:

	SrvTask(RepoTask& repo) : repo{repo} {}

	vector<Task> getAll();

	void adauga(int id, string des, string stare, string pro);

	vector<Task> cauta(string nume);

	void update(int id, string stare);
	
};