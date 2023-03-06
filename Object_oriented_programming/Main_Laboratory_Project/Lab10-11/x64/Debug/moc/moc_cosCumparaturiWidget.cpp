/****************************************************************************
** Meta object code from reading C++ file 'cosCumparaturiWidget.h'
**
** Created by: The Qt Meta Object Compiler version 68 (Qt 6.3.0)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include <memory>
#include "../../../cosCumparaturiWidget.h"
#include <QtCore/qbytearray.h>
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'cosCumparaturiWidget.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 68
#error "This file was generated using the moc from 6.3.0. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
QT_WARNING_PUSH
QT_WARNING_DISABLE_DEPRECATED
struct qt_meta_stringdata_CosCumparaturiWidget_t {
    const uint offsetsAndSize[2];
    char stringdata0[21];
};
#define QT_MOC_LITERAL(ofs, len) \
    uint(offsetof(qt_meta_stringdata_CosCumparaturiWidget_t, stringdata0) + ofs), len 
static const qt_meta_stringdata_CosCumparaturiWidget_t qt_meta_stringdata_CosCumparaturiWidget = {
    {
QT_MOC_LITERAL(0, 20) // "CosCumparaturiWidget"

    },
    "CosCumparaturiWidget"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_CosCumparaturiWidget[] = {

 // content:
      10,       // revision
       0,       // classname
       0,    0, // classinfo
       0,    0, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

       0        // eod
};

void CosCumparaturiWidget::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    (void)_o;
    (void)_id;
    (void)_c;
    (void)_a;
}

const QMetaObject CosCumparaturiWidget::staticMetaObject = { {
    QMetaObject::SuperData::link<QWidget::staticMetaObject>(),
    qt_meta_stringdata_CosCumparaturiWidget.offsetsAndSize,
    qt_meta_data_CosCumparaturiWidget,
    qt_static_metacall,
    nullptr,
qt_incomplete_metaTypeArray<qt_meta_stringdata_CosCumparaturiWidget_t
, QtPrivate::TypeAndForceComplete<CosCumparaturiWidget, std::true_type>



>,
    nullptr
} };


const QMetaObject *CosCumparaturiWidget::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *CosCumparaturiWidget::qt_metacast(const char *_clname)
{
    if (!_clname) return nullptr;
    if (!strcmp(_clname, qt_meta_stringdata_CosCumparaturiWidget.stringdata0))
        return static_cast<void*>(this);
    return QWidget::qt_metacast(_clname);
}

int CosCumparaturiWidget::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QWidget::qt_metacall(_c, _id, _a);
    return _id;
}
struct qt_meta_stringdata_CosCRUDGUI_t {
    const uint offsetsAndSize[2];
    char stringdata0[11];
};
#define QT_MOC_LITERAL(ofs, len) \
    uint(offsetof(qt_meta_stringdata_CosCRUDGUI_t, stringdata0) + ofs), len 
static const qt_meta_stringdata_CosCRUDGUI_t qt_meta_stringdata_CosCRUDGUI = {
    {
QT_MOC_LITERAL(0, 10) // "CosCRUDGUI"

    },
    "CosCRUDGUI"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_CosCRUDGUI[] = {

 // content:
      10,       // revision
       0,       // classname
       0,    0, // classinfo
       0,    0, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

       0        // eod
};

void CosCRUDGUI::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    (void)_o;
    (void)_id;
    (void)_c;
    (void)_a;
}

const QMetaObject CosCRUDGUI::staticMetaObject = { {
    QMetaObject::SuperData::link<CosCumparaturiObservator::staticMetaObject>(),
    qt_meta_stringdata_CosCRUDGUI.offsetsAndSize,
    qt_meta_data_CosCRUDGUI,
    qt_static_metacall,
    nullptr,
qt_incomplete_metaTypeArray<qt_meta_stringdata_CosCRUDGUI_t
, QtPrivate::TypeAndForceComplete<CosCRUDGUI, std::true_type>



>,
    nullptr
} };


const QMetaObject *CosCRUDGUI::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *CosCRUDGUI::qt_metacast(const char *_clname)
{
    if (!_clname) return nullptr;
    if (!strcmp(_clname, qt_meta_stringdata_CosCRUDGUI.stringdata0))
        return static_cast<void*>(this);
    return CosCumparaturiObservator::qt_metacast(_clname);
}

int CosCRUDGUI::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = CosCumparaturiObservator::qt_metacall(_c, _id, _a);
    return _id;
}
QT_WARNING_POP
QT_END_MOC_NAMESPACE
