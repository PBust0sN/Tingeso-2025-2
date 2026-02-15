import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const useGlobalShortcuts = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const handleKeyDown = (event) => {
      // Shift+N: Ir a nuevo préstamo
      if ((event.shiftKey || event.metaKey) && event.key.toLowerCase() === 'n') {
        event.preventDefault();
        navigate('/client/list/select');
      }

      // Shift+H: Ir al home
      if ((event.shiftKey || event.metaKey) && event.key.toLowerCase() === 'h') {
        event.preventDefault();
        navigate('/');
      }

      // Shift+B: devolverser a la página anterior
      if ((event.shiftKey || event.metaKey) && event.key.toLowerCase() === 'b') {
        event.preventDefault();
        navigate(-1);
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [navigate]);
};
