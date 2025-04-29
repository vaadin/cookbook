import './_shared/recipe-view.js';
import { Outlet } from 'react-router';

export function MainLayout() {
  return (
    <recipe-view>
      <Outlet />
    </recipe-view>
  );
}
