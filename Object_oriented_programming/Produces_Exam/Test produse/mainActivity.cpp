#include "mainActivity.h"
#include <qformlayout.h>

#include <qmessagebox.h>

MainActivity::MainActivity(QWidget* parent) : QWidget(parent)
{
	tableView = new QTableView;
	tableModel = new TableModel(srv.getAll());
	tableView->setModel(tableModel);

	slider->setMaximum(100);
	slider->setMinimum(1);
	slider->setOrientation(Qt::Horizontal);

	QFormLayout* fLay = new QFormLayout;
	fLay->addRow("Id", linieID);
	fLay->addRow("Nume", linieNume);
	fLay->addRow("Tip", linieTip);
	fLay->addRow("Pret", liniePret);
	fLay->addWidget(butonaduaga);
	fLay->addWidget(slider);

	QWidget* op = new QWidget;
	op->setLayout(fLay);


	tipuri.clear();
	auto v = srv.getAll();
	for (auto each : v)
	{
		string tip = each.getTip();
		if (std::find(tipuri.begin(), tipuri.end(), tip) == tipuri.end())
		{
			TipObs* o = new TipObs(tip, srv);
			obs.push_back(o);
			o->show();
			tipuri.push_back(tip);
		}
	}


	QHBoxLayout* hLay = new QHBoxLayout;
	hLay->addWidget(tableView);
	hLay->addWidget(op);
	
	this->setLayout(hLay);

	connect();
}

void MainActivity::connect()
{
	QObject::connect(butonaduaga, &QPushButton::clicked, [this]() {

		int id = linieID->text().toInt();
		string nume = linieNume->text().toStdString();
		string tip = linieTip->text().toStdString();
		double pret = liniePret->text().toDouble();
		
		try
		{
			srv.adauga(id, nume, tip, pret);
			tableModel->setProduse(srv.getAll());
			for (auto each : obs)
			{
				notifyObserver(each);
			}
		}
		catch (string err)
		{
			QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(err));
		}

		});

	QObject::connect(slider, &QSlider::valueChanged, [&]() {
		
		int val = slider->value();
		tableModel->setProduse(srv.getAll(), val);

		});
}