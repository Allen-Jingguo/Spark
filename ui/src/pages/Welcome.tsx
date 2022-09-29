import { PageContainer } from '@ant-design/pro-components';
import { Card } from 'antd';
import React from 'react';


const Welcome: React.FC = () => {
  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            'radial-gradient(circle at 97% 10%, #EBF2FF 0%, #F5F8FF 28%, #EBF1FF 124%)',
        }}
      >
        <div
          style={{
            backgroundPosition: '100% -30%',
            backgroundRepeat: 'no-repeat',
            backgroundSize: '274px auto',
            height:400,
            fontSize:30,
            display: 'flex', justifyContent: 'center', alignItems: 'center'
          }}
        >
              This is Spark Team , Welcome  to Data Flow Management System 
        </div>
      </Card>
    </PageContainer>
  );
};

export default Welcome;
