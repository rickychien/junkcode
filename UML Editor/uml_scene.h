#ifndef UML_SCENE_H
#define UML_SCENE_H

#include <QtGui/QGraphicsSceneMouseEvent>
#include <QtGui/QGraphicsScene>
#include <QtCore/QList>
#include <QtCore/QSet>
#include "uml_mode.h"
#include "uml_create_mode.h"
#include "uml_link_mode.h"
#include "uml_select_mode.h"
#include "uml_class.h"
#include "uml_usecase.h"
#include "uml_assoc_line.h"
#include "uml_general_line.h"
#include "uml_composit_line.h"
#include "uml_object_group.h"

class UMLScene : public QGraphicsScene {
public:
	UMLScene();
	~UMLScene();
	void changeUMLObjectName();

	// Default QGraphicsScene Mouse Event
	void defaultMousePressEvent( QGraphicsSceneMouseEvent * event );
	void defaultMouseMoveEvent( QGraphicsSceneMouseEvent * event );
	void defaultMouseReleaseEvent( QGraphicsSceneMouseEvent * event );

	// Grouping
	QList< QGraphicsItem * > getGroupingList();
	void groupUMLObjects();
	void unGroupUMLObjects();

	QList< UMLObject * > umlObjectList;
	QList< UMLLink * > umlLinkList;
	UMLMode * mode;

protected:
	virtual void mousePressEvent( QGraphicsSceneMouseEvent * event );
	virtual void mouseMoveEvent( QGraphicsSceneMouseEvent * event );
	virtual void mouseReleaseEvent( QGraphicsSceneMouseEvent * event );

private:
	QPointF _mouseSelectedBeginPoint;

	QGraphicsRectItem * _mouseSelectedRect;
	QGraphicsLineItem * _mouseMoveLine;
	UMLObjectPort * _startPort;
	UMLObject * _startUMLObject;
};

#endif
