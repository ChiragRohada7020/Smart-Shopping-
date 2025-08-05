import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white py-6">
      <div className="container mx-auto px-4 text-center">
        <p>&copy; {new Date().getFullYear()} PriceCompare. All rights reserved.</p>
        <div className="flex justify-center space-x-4 mt-4">
          <a href="#" className="hover:text-blue-400">Privacy Policy</a>
          <a href="#" className="hover:text-blue-400">Terms of Service</a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;