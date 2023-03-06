#include "mainActivity.h"
#include <qformlayout.h>
#include <qmessagebox.h>

MainActivity::MainActivity(QWidget* parent) : QWidget(parent)
{
	setMouseTracking(true);

	tableModel = new TableModel(srv.getAll());
	tableView = new QTableView;
	tableView->setModel(tableModel);

	QFormLayout* fLay = new QFormLayout;
	fLay->addRow("ID", linieID);
	fLay->addRow("Denumire", linieDen);
	fLay->addRow("Tip", linieTip);
	fLay->addRow("Roti", linieRoti);
	fLay->addWidget(butonAduaga);
	fLay->addWidget(cBox);
	updateCombo();

	QWidget* op = new QWidget;
	op->setLayout(fLay);


	QHBoxLayout* hLay = new QHBoxLayout;
	hLay->addWidget(tableView);
	hLay->addWidget(op);

	this->setLayout(hLay);
	connect();

}


void MainActivity::connect()
{
	QObject::connect(butonAduaga, &QPushButton::clicked, [this]() {

		int id, roti;
		string den, tip;
		id = linieID->text().toInt();
		den = linieDen->text().toStdString();
		tip = linieTip->text().toStdString();
		roti = linieRoti->text().toInt();
		try
		{
			srv.adauga(id, den, tip, roti);
			tableModel->setTrac(srv.getAll());
			updateCombo();
		}
		catch (string err)
		{
			QMessageBox::warning(nullptr, "Eroare", QString::fromStdString(err));
		}


		});

	QObject::connect(cBox, &QComboBox::currentTextChanged, [this]() {

		string tip = cBox->currentText().toStdString();
		tableModel->setTrac(srv.getAll(), tip);


		});

	QObject::connect(tableView->selectionModel(), &QItemSelectionModel::selectionChanged, [this]() {
		repaint();
		});

	
}

void MainActivity::updateCombo()
{
	cBox->clear();
	auto tip = srv.tipuri();
	for (auto t : tip)
	{
		cBox->addItem(QString::fromStdString(t));
	}
}