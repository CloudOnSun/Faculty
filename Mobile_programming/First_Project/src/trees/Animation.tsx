import React, { useEffect, useRef } from 'react';
import { CreateAnimation, createAnimation } from '@ionic/react';
// import './AnimationDemo.css';
import { MyModal } from './MyModal';

const AnimationDemo: React.FC = () => {
    const elCRef = useRef(null);
    const animationRef = useRef<CreateAnimation>(null);
    useEffect(simpleAnimationJS, []);
    // useEffect(groupAnimations, []);
    // useEffect(chainAnimations, []);
    // useEffect(simpleAnimationReact, [animationRef.current]);
    return (<></>)

    function simpleAnimationJS() {
        const el = document.querySelector('.myAnimationClass');
        if (el) {
            const animation = createAnimation()
                .addElement(el)
                .duration(5000)
                .direction('alternate')
                .iterations(Infinity)
                .keyframes([
                    { offset: 0, transform: 'scale(3)', opacity: '1' },
                    { offset: 0.5, transform: 'scale(1.5)', opacity: '1' },
                    {
                        offset: 1, transform: 'scale(0.5)', opacity: '0.2'
                    }
                ]);
            animation.play();
        }
    }
};

export default AnimationDemo;
