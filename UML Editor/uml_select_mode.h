#ifndef UML_SELECT_MODE_H
#define UML_SELECT_MODE_H

#include "uml_mode.h"
#include "uml_object.h"
#include "uml_class.h"
#include "uml_usecase.h"
#include "uml_scene.h"
#include <QtGui/QGraphicsSceneMouseEvent>

class SelectMode : public UMLMode {
public:
	SelectMode();
	SelectMode( UMLScene * delegateScene );
	~SelectMode();
	virtual void onMousePress( QGraphicsSceneMouseEvent * event );
	virtual void onMouseMove( QGraphicsSceneMouseEvent * event );
	virtual void onMouseRelease( QGraphicsSceneMouseEvent * event );

private:
	QPointF _mouseSelectedBeginPoint;
	QGraphicsRectItem * _mouseSelectedRect;

};

#endif
