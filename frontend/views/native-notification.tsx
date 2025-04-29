import "../recipe/native-notification/native-notification.js";
import Tag from "../generated/com/vaadin/recipes/recipe/Tag.js";
import type { ViewConfig } from "./_shared/ViewConfig.js";
export const config: ViewConfig = {
    title: "Show a native OS-level notification",
    detail: {
        description: "Show how you display a native OS-level notification to the user.",
        tags: [Tag.TYPE_SCRIPT, Tag.HILLA]
    },
};
export default function NativeNotification() {
    return <native-notification />;
}
