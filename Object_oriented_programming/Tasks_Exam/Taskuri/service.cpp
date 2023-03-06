#include "service.h"

vector<Task> SrvTask::getAll()
{
	auto v = repo.getAll();
	sort(v.begin(), v.end(), [&](Task t1, Task t2) { return t1.getStare() < t2.getStare(); });
	return v;
}


void SrvTask::adauga(int id, string des, string stare, string pro)
{
	auto it = pro.find(',');

	vector<string> p;

	while (it < pro.length())
	{
		size_t poz = pro.find_first_of(",");
		string p2 = pro.substr(0, poz);
		pro = pro.substr(poz + 1, pro.length());
		p.push_back(p2);
		it = pro.find(',');
	}
	p.push_back(pro);

	Task t(id, des, p, stare);
	t.valideaza();
	repo.adauga(t);

}

vector<Task> SrvTask::cauta(string nume)
{
	return repo.cauta(nume);
}

void SrvTask::update(int id, string stare)
{
	repo.update(id, stare);
}