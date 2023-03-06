/********************************************************************************
** Form generated from reading UI file 'TestMelodii.ui'
**
** Created by: Qt User Interface Compiler version 6.3.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TESTMELODII_H
#define UI_TESTMELODII_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_TestMelodiiClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *TestMelodiiClass)
    {
        if (TestMelodiiClass->objectName().isEmpty())
            TestMelodiiClass->setObjectName(QString::fromUtf8("TestMelodiiClass"));
        TestMelodiiClass->resize(600, 400);
        menuBar = new QMenuBar(TestMelodiiClass);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        TestMelodiiClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(TestMelodiiClass);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        TestMelodiiClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(TestMelodiiClass);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        TestMelodiiClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(TestMelodiiClass);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        TestMelodiiClass->setStatusBar(statusBar);

        retranslateUi(TestMelodiiClass);

        QMetaObject::connectSlotsByName(TestMelodiiClass);
    } // setupUi

    void retranslateUi(QMainWindow *TestMelodiiClass)
    {
        TestMelodiiClass->setWindowTitle(QCoreApplication::translate("TestMelodiiClass", "TestMelodii", nullptr));
    } // retranslateUi

};

namespace Ui {
    class TestMelodiiClass: public Ui_TestMelodiiClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TESTMELODII_H
