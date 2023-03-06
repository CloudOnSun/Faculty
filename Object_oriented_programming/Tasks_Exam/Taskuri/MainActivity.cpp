#include "mainActivity.h"
#include <qboxlayout.h>
#include <qmessagebox.h>
#include <qformlayout.h>


MainActivity::MainActivity()  
{
	tableView = new QTableView;
	tableModel = new TableModel(srv.getAll());
	tableView->setModel(tableModel);

	linieDesc = new QLineEdit;
	linieId = new QLineEdit;
	liniePro = new QLineEdit;
	linieStare = new QLineEdit;
	linieCauta = new QLineEdit;

	butonadauga = new QPushButton("Adauga");

	QFormLayout* fLayout = new QFormLayout;
	fLayout->addRow("Id", linieId);
	fLayout->addRow("Descriere", linieDesc);
	fLayout->addRow("Stare", linieStare);
	fLayout->addRow("Programatori", liniePro);
	fLayout->addWidget(butonadauga);

	fLayout->addRow("Cauta", linieCauta);

	QWidget* operatii = new QWidget;
	operatii->setLayout(fLayout);


	QHBoxLayout* hLayout = new QHBoxLayout;
	hLayout->addWidget(tableView);
	hLayout->addWidget(operatii);

	this->setLayout(hLayout);
	connect();

	obsClosed->adaugaObs(obsProgress);
	obsClosed->adaugaObs(obsOpen);
	obsClosed->adaugaObs(this);
	obsOpen->adaugaObs(obsClosed);
	obsOpen->adaugaObs(obsProgress);
	obsOpen->adaugaObs(this);
	obsProgress->adaugaObs(obsOpen);
	obsProgress->adaugaObs(obsClosed);
	obsProgress->adaugaObs(this);
	obs.push_back(obsClosed);
	obs.push_back(obsOpen);
	obs.push_back(obsProgress);

	obsClosed->show();
	obsProgress->show();
	obsOpen->show();
}

void MainActivity::connect()
{
	QObject::connect(butonadauga, &QPushButton::clicked, [&]() {

		string des, stare, pro;
		int id;
		id = linieId->text().toInt();
		des = linieDesc->text().toStdString();
		stare = linieStare->text().toStdString();
		pro = liniePro->text().toStdString();
		try
		{
			srv.adauga(id, des, stare, pro);
			tableModel->setTask(srv.getAll());
			for (auto each : obs)
			{
				notifyObserver(each);
			}
		}
		catch (int er)
		{
			QMessageBox::warning(nullptr, "Eroare", "Date invalide");
		}
		});

	QObject::connect(linieCauta, &QLineEdit::textChanged, [&]() {

		if (linieCauta->text().toStdString().length() <= 0)
		{
			tableModel->setTask(srv.getAll());
			return;
		}
		string nume = linieCauta->text().toStdString();
		tableModel->setTask(srv.cauta(nume));
		for (auto each : obs)
		{
			notifyObserver(each);
		}

		});
}


void MainActivity::update()
{
	tableModel->setTask(srv.getAll());
}