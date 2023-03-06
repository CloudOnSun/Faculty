#include "repository.h"
#include <fstream>

void RepoTask::citesteDinFisier()
{
	ifstream in(file);
	taskuri.clear();
	string linie, des, stare, p;
	int id;
	vector <string> pro;
	while (getline(in, linie))
	{
		pro.clear();
		size_t poz = linie.find_first_of(",");
		id = stoi(linie.substr(0, poz));
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		des = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		stare = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		auto it = linie.find(',');

		while (it < linie.length())
		{
			poz = linie.find_first_of(",");
			p = linie.substr(0, poz);
			linie = linie.substr(poz + 1, linie.length());
			pro.push_back(p);
			it = linie.find(',');
		}
		pro.push_back(linie);
		Task t(id, des, pro, stare);
		taskuri.push_back(t);
	}

}


void RepoTask::incarcaInFisier()
{
	ofstream out(file);
	for (auto t : taskuri)
	{
		out << t.toString() << endl;
	}
}


vector <Task> RepoTask::getAll()
{
	return taskuri;
}

void RepoTask::adauga(Task t)
{
	for (auto t2 : taskuri)
	{
		if (t2.getId() == t.getId())
			throw (1);
	}

	taskuri.push_back(t);
	incarcaInFisier();
}

vector<Task> RepoTask::cauta(string nume)
{
	vector<Task> v;
	for (auto t : taskuri)
	{
		auto pro = t.getPro();
		if (find(pro.begin(), pro.end(), nume) != pro.end())
		{
			v.push_back(t);
		}
	}
	return v;
}

void RepoTask::update(int id, string stare)
{
	for (auto& t : taskuri)
	{
		if (t.getId() == id)
		{
			t.setStare(stare);
			incarcaInFisier();
			break;
		}
	}
}