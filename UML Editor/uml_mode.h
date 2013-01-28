#ifndef UML_MODE_H
#define UML_MODE_H

class UMLScene;
class QGraphicsSceneMouseEvent;

class UMLMode {
public:
	UMLMode();
	UMLMode( UMLScene * delegateScene );
	virtual void onMousePress( QGraphicsSceneMouseEvent * event );
	virtual void onMouseMove( QGraphicsSceneMouseEvent * event );
	virtual void onMouseRelease( QGraphicsSceneMouseEvent * event );

protected:
	UMLScene * delegateScene;

};

#endif
