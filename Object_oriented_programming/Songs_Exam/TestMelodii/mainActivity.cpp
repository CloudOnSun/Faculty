#include "mainActivity.h"
#include <qlayout.h>
#include <qslider.h>
#include <qmessagebox.h>
#include <qpainter.h>


MainActivity::MainActivity(QWidget* parent) : QWidget(parent)
{
	tableModel = new MyTableModel(srv.getAll());
	tableView = new QTableView;
	tableView->setModel(tableModel);
	tableView->setMaximumHeight(150);

	titluEdit = new QLineEdit;
	slider = new QSlider;
	slider->setMaximum(10);
	slider->setMinimum(0);
	slider->setOrientation(Qt::Horizontal);
	butonUpdate = new QPushButton("Update");
	butonSterge = new QPushButton("Sterge");

	QVBoxLayout* vLayout = new QVBoxLayout;
	vLayout->addWidget(titluEdit);
	vLayout->addWidget(slider);
	vLayout->addWidget(butonUpdate);
	vLayout->addWidget(butonSterge);

	QWidget* operatii = new QWidget;
	operatii->setLayout(vLayout);

	QHBoxLayout* hLayout = new QHBoxLayout;
	hLayout->addWidget(tableView);
	hLayout->addWidget(operatii);
	this->setLayout(hLayout);

	connect();

}

void MainActivity::connect()
{
	QObject::connect(tableView->selectionModel(), &QItemSelectionModel::selectionChanged, [&]() {this->itemSelectat(); });
	QObject::connect(butonUpdate, &QPushButton::clicked, [&]() {this->update(); });
	QObject::connect(butonSterge, &QPushButton::clicked, [&]() {this->sterge(); });
}

void MainActivity::update()
{
	if (tableView->selectionModel()->selectedIndexes().isEmpty())
		return;
	int row = tableView->selectionModel()->selectedIndexes().at(0).row();
	auto celIndex = tableView->model()->index(row, 0);
	auto id = tableView->model()->data(celIndex, Qt::DisplayRole).toInt();
	srv.update(id, titluEdit->text().toStdString(), slider->value());
	tableModel->set(srv.getAll());
	repaint();
}

void MainActivity::itemSelectat()
{
	if (tableView->selectionModel()->selectedIndexes().isEmpty())
		return;
	int row = tableView->selectionModel()->selectedIndexes().at(0).row();
	auto celIndex= tableView->model()->index(row, 1);
	auto cel2Index = tableView->model()->index(row, 3);
	auto titlu = tableView->model()->data(celIndex, Qt::DisplayRole).toString();
	auto rank = tableView->model()->data(cel2Index, Qt::DisplayRole).toInt();
	titluEdit->setText(titlu);
	slider->setValue(rank);
}

void MainActivity::sterge()
{
	if (tableView->selectionModel()->selectedIndexes().isEmpty())
		return;
	int row = tableView->selectionModel()->selectedIndexes().at(0).row();
	auto celIndex = tableView->model()->index(row, 0);
	auto id = tableView->model()->data(celIndex, Qt::DisplayRole).toInt();
	auto cel2Index = tableView->model()->index(row, 2);
	auto artist = tableView->model()->data(cel2Index, Qt::DisplayRole).toString().toStdString();
	try
	{
		srv.sterge(id, artist);
		tableModel->set(srv.getAll());
		repaint();
	}
	catch (int a)
	{
		QMessageBox::information(nullptr, "Eroare", "Aristul are doar o melodie");
	}
}

void MainActivity::paintEvent(QPaintEvent* ev)
{
	QPainter p{ this };
	vector<int> inaltime;
	for (int i = 0; i <= 10; i++)
	{
		inaltime.push_back(0);
	}
	auto melodii = srv.getAll();
	for (auto m : melodii)
	{
		int ina = m.getRank();
		inaltime[ina]++;
	}
	p.drawLine(50, height(), 50, height() - inaltime[0] * 10);
	p.drawLine(100, height(), 100, height() - inaltime[1] * 10);
	p.drawLine(150, height(), 150, height() - inaltime[2] * 10);
	p.drawLine(200, height(), 200, height() - inaltime[3] * 10);
	p.drawLine(250, height(), 250, height() - inaltime[4] * 10);
	p.drawLine(300, height(), 300, height() - inaltime[5] * 10);
	p.drawLine(350, height(), 350, height() - inaltime[6] * 10);
	p.drawLine(400, height(), 400, height() - inaltime[7] * 10);
	p.drawLine(450, height(), 450, height() - inaltime[8] * 10);
	p.drawLine(500, height(), 500, height() - inaltime[9] * 10);
	p.drawLine(550, height(), 550, height() - inaltime[10] * 10);
	
	
}