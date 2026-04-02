import { Navigate } from 'react-router-dom';

type ProtectedRouteProps = {
  children: React.ReactNode;
  allowedRoles?: string[];
};

export function ProtectedRoute({
  children,
  allowedRoles,
}: ProtectedRouteProps) {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  //not logged in
  if (!token) {
    return <Navigate to="/login" />;
  }

  //logged in but wrong role
  if (allowedRoles && !allowedRoles.includes(role || '')) {
    return <Navigate to="/dashboard" />;
  }

  return <>{children}</>;
}
