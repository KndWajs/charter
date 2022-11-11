import {Card, Elevation} from "@blueprintjs/core"

import React from "react"
import "./menu.scss"
import {useTranslation} from "react-i18next";


export default function Menu() {
    const {t, i18n} = useTranslation();
    const lngs: any = {
        en: {nativeName: 'English'},
        pl: {nativeName: 'Polski'}
    };

    return (
        <Card id="main-card" interactive={false} elevation={Elevation.TWO}>
            <div className="bp4-button-group .modifier">
                {Object.keys(lngs).map((lng) => (
                    <a className="bp4-button bp4-intent-success"
                       key={lng}

                       style={{fontWeight: i18n.resolvedLanguage === lng ? 'bold' : 'normal'}}
                       type="submit"
                       onClick={() => i18n.changeLanguage(lng)}>
                        {lngs[lng].nativeName}
                    </a>
                ))}
            </div>
            <div>

            </div>
        </Card>

    )
}
